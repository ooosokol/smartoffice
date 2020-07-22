package ru.sokol.smartoffice.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.sokol.smartoffice.model.device.Device;
import ru.sokol.smartoffice.model.device.DeviceControlRequest;
import ru.sokol.smartoffice.model.device.DeviceControlResponse;
import ru.sokol.smartoffice.model.device.DeviceEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DevicesServiceImpl {

    private final DeviceApiClientService deviceApiClientService;

    public DevicesServiceImpl(DeviceApiClientService deviceApiClientService) {
        this.deviceApiClientService = deviceApiClientService;
    }

    public List<Device> getListedDevices(){
        return Arrays.stream(DeviceEnum.values()).filter(DeviceEnum::getListed).map(DeviceEnum::getDevice).collect(Collectors.toList());
    }

    public void sendRequestAndNotify(DeviceControlRequest request){
        Mono.just(new DeviceControlResponse())
        /*deviceApiClientService.processRequest(request)*/.subscribe((response)->{
//            request.getDevice().getDevice()
            Device device = request.getDevice().getDevice();
//            device.setNewState();
//            template.convertAndSend("/topic/devices", device);
        });
    }



}
