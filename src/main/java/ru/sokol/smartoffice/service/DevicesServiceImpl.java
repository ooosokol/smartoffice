package ru.sokol.smartoffice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sokol.smartoffice.model.device.Device;
import ru.sokol.smartoffice.model.deviceControlApiModel.DeviceControlRequest;
import ru.sokol.smartoffice.model.device.DeviceEnum;
import ru.sokol.smartoffice.sender.DeviceApiBusSender;

import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DevicesServiceImpl {

    private final ObjectMapper objectMapper;
    private final DeviceApiBusSender deviceApiBusSender;

    public DevicesServiceImpl(ObjectMapper objectMapper, DeviceApiBusSender deviceApiBusSender) {
        this.objectMapper = objectMapper;
        this.deviceApiBusSender = deviceApiBusSender;
    }

    public List<Device> getListedDevices(){
        return Arrays.stream(DeviceEnum.values()).filter(DeviceEnum::getListed).map(DeviceEnum::getDevice).collect(Collectors.toList());
    }



    @SneakyThrows
    public synchronized boolean sendRequest(DeviceControlRequest request){
        deviceApiBusSender.send(request);
       /* String requestString = objectMapper.writeValueAsString(request);
        log.info(requestString);



        SerialPort serialPort = SerialPort.getCommPorts()[1];;
        serialPort.setBaudRate(115200);
        serialPort.openPort();

        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
        try {
            serialPort.writeBytes(requestString.getBytes(),requestString.length());
        } catch (Exception e) { e.printStackTrace(); }

        serialPort.closePort();
*/
        /*Mono.just(new DeviceControlResponse())
        *//*deviceApiClientService.processRequest(request)*//*.subscribe((response)->{
//            request.getDevice().getDevice()
//            Device device = request.getDevice().getDevice();
//            device.setNewState();
//            template.convertAndSend("/topic/devices", device);
        });*/
        //TODO check 200 status code
        return true;
    }

    @PreDestroy
    void destroy(){

    }
}
