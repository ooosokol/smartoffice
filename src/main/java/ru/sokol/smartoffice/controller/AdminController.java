package ru.sokol.smartoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sokol.smartoffice.model.device.DeviceEnum;
import ru.sokol.smartoffice.model.deviceControlApiModel.DeviceControlRequest;
import ru.sokol.smartoffice.model.deviceControlApiModel.HardwareDeviceEnum;
import ru.sokol.smartoffice.service.DevicesServiceImpl;


@Controller
public class AdminController {
    private final DevicesServiceImpl devicesService;

    public AdminController(DevicesServiceImpl devicesService) {
        this.devicesService = devicesService;
    }

    @GetMapping("/uniqueAdminController/activateLaser")
    @ResponseBody
    public String  activateLaser(){
        DeviceControlRequest laserActivateRequest = new DeviceControlRequest();
        laserActivateRequest.setDevice(HardwareDeviceEnum.sw10);
        laserActivateRequest.setPower(true);
        devicesService.sendRequest(laserActivateRequest);
        DeviceEnum.LASER_POWER.getDevice().setPower(true);
        return "turn on";
    }
}
