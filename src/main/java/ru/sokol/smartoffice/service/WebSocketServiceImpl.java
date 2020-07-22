package ru.sokol.smartoffice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.sokol.smartoffice.model.device.Device;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WebSocketServiceImpl {
    private final Map<String, WebSocketSession> connectionsMap = new ConcurrentHashMap<>();

    private final DevicesServiceImpl devicesService;
    private final ObjectMapper objectMapper;

    private final WebSocketMessage<?> pingMessage = new TextMessage("{\"action\":\"ping\"}");

    public WebSocketServiceImpl(DevicesServiceImpl devicesService, ObjectMapper objectMapper) {
        this.devicesService = devicesService;
        this.objectMapper = objectMapper;
    }


    public void addClient(WebSocketSession session){
        connectionsMap.put(session.getId(),session);
        sendMessage(session, convertToWebsocketMessage(devicesService.getListedDevices()));
    }

    public void dropClient(WebSocketSession session){
        connectionsMap.remove(session.getId());
    }

    public void sendNewDeviceStatus(Device device){
        WebSocketMessage<?> message = convertToWebsocketMessage(device);
        connectionsMap.values().parallelStream().forEach(session -> sendMessage(session,message));
    }

    @Scheduled(fixedRate = 30_000)
    public void sendPingMessage(){
        connectionsMap.values().parallelStream().forEach(session -> sendMessage(session,pingMessage));
    }

    @SneakyThrows
    private void sendMessage(WebSocketSession session, WebSocketMessage<?> message){
        session.sendMessage(message);
    }

    @SneakyThrows
    private WebSocketMessage<?> convertToWebsocketMessage(Object payload){
        return new TextMessage(objectMapper.writeValueAsString(payload));
    }
}
