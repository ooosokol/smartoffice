package ru.sokol.smartoffice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import ru.sokol.smartoffice.service.DevicesServiceImpl;

@Slf4j
@Controller
public class DeviceController {

    private final DevicesServiceImpl devicesService;

    public DeviceController(DevicesServiceImpl devicesService) {
        this.devicesService = devicesService;
    }

  /*  @GetMapping("/devices")
    @ResponseBody
    public Collection<Device> getDevicesStatus() {
        return devicesService.getListedDevices();
    }
*/
/*

    @SubscribeMapping("/devices")
    public Collection<Device> chatInit() {
        return devicesService.getListedDevices();
    }

    @MessageMapping("/devices")
    @SendToUser
    public String  processDeviceCommand(Message<ClientDeviceRequest> request) throws Exception {
        log.info("request: {}",request);

//        DeviceControlRequest deviceControlRequest = new DeviceControlRequest();
        switch (request.getPayload().getIdentifier().getDevice().getDeviceClass()) {
            case LED:
//                deviceControlRequest.setColor(request.getColor());
            case SWITCH:
//                deviceControlRequest.setPower(request.getPower());
                break;
            default:
                return "FAIL";
        }
        */
/*deviceControlRequest.setDevice(request.getIdentifier());

        devicesService.sendRequestAndNotify(deviceControlRequest);

        this.template.convertAndSend("/topic/devices", DeviceEnum.SWITCH1.getDevice());*//*
*/
/*
        *//*
return "something";

    }
*/


}
