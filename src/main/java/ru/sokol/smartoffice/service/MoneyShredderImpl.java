package ru.sokol.smartoffice.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sokol.smartoffice.model.deviceControlApiModel.DeviceControlRequest;
import ru.sokol.smartoffice.model.deviceControlApiModel.HardwareDeviceEnum;

import java.time.Duration;

@Service
public class MoneyShredderImpl {
    private final DevicesServiceImpl devicesService;
    private Integer duration = 3500;

    public MoneyShredderImpl(DevicesServiceImpl devicesService) {
        this.devicesService = devicesService;
    }

//    @Scheduled(fixedRate = 60_000, initialDelay = 120_000)
    public void runShredder(){
        DeviceControlRequest request = new DeviceControlRequest();
        request.setDevice(HardwareDeviceEnum.sw14);
        request.setPower(true);
        request.setPeriod((int) Duration.ofMillis(duration).toMillis());
        devicesService.sendRequest(request);


    }
}
