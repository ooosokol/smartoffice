package ru.sokol.smartoffice.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.sokol.smartoffice.model.Device;
import ru.sokol.smartoffice.model.DeviceControlRequest;
import ru.sokol.smartoffice.model.DeviceControlResponse;
import ru.sokol.smartoffice.model.DeviceEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DevicesServiceImpl {

    private final DeviceApiClientService deviceApiClientService;
    private final SimpMessagingTemplate template;

    public DevicesServiceImpl(DeviceApiClientService deviceApiClientService, SimpMessagingTemplate template) {
        this.deviceApiClientService = deviceApiClientService;
        this.template = template;
    }

    public List<Device> getListedDevices(){
        return Arrays.stream(DeviceEnum.values()).filter(DeviceEnum::getListed).map(DeviceEnum::getDevice).collect(Collectors.toList());
    }

    public void sendRequestAndNotify(){
        Mono.just(new DeviceControlResponse())
        /*deviceApiClientService.processRequest(new DeviceControlRequest())*/.subscribe((response)->{
            template.convertAndSend("/topic/devices", response);
        });
    }



}
