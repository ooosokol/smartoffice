package ru.sokol.smartoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sokol.smartoffice.model.device.DeviceEnum;
import ru.sokol.smartoffice.model.deviceControlApiModel.DeviceControlRequest;
import ru.sokol.smartoffice.model.deviceControlApiModel.HardwareDeviceEnum;
import ru.sokol.smartoffice.service.DevicesServiceImpl;
import ru.sokol.smartoffice.service.FanServiceImpl;


@Controller
public class AdminController {
    private final DevicesServiceImpl devicesService;
    private final FanServiceImpl fanService;

    public AdminController(DevicesServiceImpl devicesService, FanServiceImpl fanService) {
        this.devicesService = devicesService;
        this.fanService = fanService;
    }

    @GetMapping("/uniqueAdminController/activateLaser")
    @ResponseBody
    public String  activateLaser(){
        DeviceControlRequest laserActivateRequest = new DeviceControlRequest();
        laserActivateRequest.setDevice(HardwareDeviceEnum.sw10);
        laserActivateRequest.setPower(true);
        devicesService.sendRequest(laserActivateRequest);
        DeviceEnum.LASER_POWER.getDevice().setPower(true);

        if(fanService.getCurrentPhase() < (short) 3){
            fanService.setCurrentPhase((short)3);
        }
        return "turn on";
    }
}
