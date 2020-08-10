package ru.sokol.smartoffice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.sokol.smartoffice.model.clientModels.ClientDeviceRequest;
import ru.sokol.smartoffice.model.device.AbstractDevice;
import ru.sokol.smartoffice.model.device.Device;
import ru.sokol.smartoffice.model.device.DeviceEnum;
import ru.sokol.smartoffice.model.device.LedDevice;
import ru.sokol.smartoffice.model.deviceControlApiModel.DeviceControlRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
@Slf4j
public class WebSocketServiceImpl {
    private final Map<String, WebSocketSession> connectionsMap = new ConcurrentHashMap<>();

    private final DevicesServiceImpl devicesService;
    private final ObjectMapper objectMapper;
    private final FanServiceImpl fanService;

    private final WebSocketMessage<?> PING_MESSAGE = new TextMessage("{\"action\":\"ping\"}");
    private final WebSocketMessage<?> UNKNOWN_DEVICE = new TextMessage("{\"message\":\"unknown device\"}");
    private final WebSocketMessage<?> INVALID_CREDENTIALS = new TextMessage("{\"message\":\"invalid credentials\"}");
    private final WebSocketMessage<?> DEVICE_NOT_READY = new TextMessage("{\"message\":\"Подождите 5 секунд\"}");
    private final WebSocketMessage<?> BAD_REQUEST = new TextMessage("{\"message\":\"bad request\"}");

    Pattern COLOR_PATTERN = Pattern.compile("^[A-Fa-f0-9]{6}$");

    public WebSocketServiceImpl(DevicesServiceImpl devicesService, ObjectMapper objectMapper, FanServiceImpl fanService) {
        this.devicesService = devicesService;
        this.objectMapper = objectMapper;
        this.fanService = fanService;
    }


    public void addClient(WebSocketSession session) {
        connectionsMap.put(session.getId(), session);
        sendMessage(session, convertToWebsocketMessage(devicesService.getListedDevices()));
    }

    public void dropClient(WebSocketSession session) {
        connectionsMap.remove(session.getId());
    }

    public void sendNewDeviceStatus(Device device) {
        WebSocketMessage<?> message = convertToWebsocketMessage(device);
        connectionsMap.values().parallelStream().forEach(session -> sendMessage(session, message));
    }

    public void handleClientRequest(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        var request = objectMapper.readValue(message.getPayload(), ClientDeviceRequest.class);
        if ("pong".equals(request.getAction())) {
            return;
        }
//        validate request here
        if (request.getDevice() == null ||
                DeviceEnum.LASER.equals(request.getDevice()) ||
                DeviceEnum.FAN1.equals(request.getDevice()) ||
                DeviceEnum.FAN2.equals(request.getDevice())) {
            sendMessage(session, UNKNOWN_DEVICE);
            return;
        }
        if (request.getDevice().getAuthorized() && (!"admin".equals(request.getLogin()) || !"admin".equals(request.getPassword()))) {
            sendMessage(session, INVALID_CREDENTIALS);
            return;
        }
        if (request.getPower() == null ||(request.getColor()!=null && !COLOR_PATTERN.matcher(request.getColor()).matches())) {
            sendMessage(session, BAD_REQUEST);
            return;
        }
        AbstractDevice device = request.getDevice().getDevice();
        boolean success = false;
        synchronized (request.getDevice()) {
            if (!request.getDevice().getDevice().isDeviceReady()) {
                sendMessage(session, DEVICE_NOT_READY);
                return;
            }
            if(DeviceEnum.SWITCH2.equals(request.getDevice()) && !request.getPower()){
                sendMessage(session, DEVICE_NOT_READY);
                return;
            }
            var deviceControlRequest = new DeviceControlRequest();
            if(DeviceEnum.SWITCH2.equals(request.getDevice())){
                if(fanService.getCurrentPhase() < (short) 2){
                    fanService.setCurrentPhase((short)2);
                }
                deviceControlRequest.setProcess(true);
                deviceControlRequest.setState(fanService.getCurrentPhase());
            }
            deviceControlRequest.setDevice(request.getDevice().getHardwareDevice());


            deviceControlRequest.setPower(request.getPower());
            if (LedDevice.class.equals(request.getDevice().getDevice().getDeviceClass())) {
                deviceControlRequest.setColor(request.getColor() != null ? request.getColor() :
                        ((LedDevice) device).getColor());
                deviceControlRequest.setStart(request.getDevice().getStart());
                deviceControlRequest.setEnd(request.getDevice().getEnd());
                deviceControlRequest.setLevel(request.getDevice().getLevel());
            }

            if (devicesService.sendRequest(deviceControlRequest)) {
                success = true;
                device.changeDevice();
                device.setPower(request.getPower());
                if (LedDevice.class.equals(request.getDevice().getDevice().getDeviceClass()) && request.getColor() != null) {
                    ((LedDevice) device).setColor(request.getColor());
                }
            }
        }
        if (success) {
            sendNewDeviceStatus(device);
        }

    }

    @Scheduled(fixedRate = 10_000)
    public void sendPingMessage() {
        connectionsMap.values().parallelStream().forEach(session -> sendMessage(session, PING_MESSAGE));
    }

    @SneakyThrows
    private void sendMessage(WebSocketSession session, WebSocketMessage<?> message) {
        session.sendMessage(message);
    }

    @SneakyThrows
    private WebSocketMessage<?> convertToWebsocketMessage(Object payload) {
        return new TextMessage(objectMapper.writeValueAsString(payload));
    }
}
