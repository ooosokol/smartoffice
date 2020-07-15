package ru.sokol.smartoffice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sokol.smartoffice.model.clientModels.ClientDeviceRequest;
import ru.sokol.smartoffice.model.device.Device;
import ru.sokol.smartoffice.model.device.DeviceControlRequest;
import ru.sokol.smartoffice.model.device.DeviceEnum;
import ru.sokol.smartoffice.service.DevicesServiceImpl;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@Controller
public class DeviceController {

    private final DevicesServiceImpl devicesService;
    private final SimpMessagingTemplate template;

    public DeviceController(DevicesServiceImpl devicesService, SimpMessagingTemplate template) {
        this.devicesService = devicesService;
        this.template = template;
    }

    @GetMapping("/devices")
    @ResponseBody
    public Collection<Device> getDevicesStatus() {
        return devicesService.getListedDevices();
    }


    @SubscribeMapping("/devices")
    public Collection<Device> chatInit() {
        return devicesService.getListedDevices();
    }

    @MessageMapping("/devices")
    public void processDeviceCommand(@Valid ClientDeviceRequest request) {
        DeviceControlRequest deviceControlRequest = new DeviceControlRequest();
        switch (request.getIdentifier().getDevice().getDeviceClass()) {
            case LED:
                deviceControlRequest.setColor(request.getColor());
            case SWITCH:
                deviceControlRequest.setPower(request.getPower());
                break;
            default:
                return;
        }
        deviceControlRequest.setDevice(request.getIdentifier());

        devicesService.sendRequestAndNotify(deviceControlRequest);

        this.template.convertAndSend("/topic/devices", DeviceEnum.SWITCH1.getDevice());

    }


}
