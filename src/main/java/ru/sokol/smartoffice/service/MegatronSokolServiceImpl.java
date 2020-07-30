package ru.sokol.smartoffice.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import ru.sokol.smartoffice.model.device.DeviceEnum;
import ru.sokol.smartoffice.model.deviceControlApiModel.DeviceControlRequest;
import ru.sokol.smartoffice.service.exception.MegatronException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MegatronSokolServiceImpl  {
    private static final String secret = "04080F10172A";
    private final AtomicLong counter;

    private final DevicesServiceImpl devicesService;

    private final DeviceEnum megatronLaserSwitch = DeviceEnum.SWITCH2;


    public MegatronSokolServiceImpl(DevicesServiceImpl devicesService) {
        this.devicesService = devicesService;
        counter = new AtomicLong(43);
    }


    public String getMegatronTestToken() {
        return DigestUtils.md5Hex(DigestUtils.md5Hex(String.valueOf(counter.get())));
    }

    private String getMegatronToken() {
        return DigestUtils.md5Hex(DigestUtils.md5Hex(counter.get() + secret));
    }

    public boolean getMegatronPowerState(){
        return megatronLaserSwitch.getDevice().getPower();
    }



    public void startMegatron(String megatronToken) throws MegatronException {
        if (getMegatronToken().equals(megatronToken)) {
            DeviceControlRequest request = new DeviceControlRequest();
            request.setDevice(DeviceEnum.LASER);
            request.setPower(true);
            request.setLevel((short)100);
            request.setPeriod((int) Duration.ofMinutes(20).toSeconds());

            devicesService.sendRequest(request);
            DeviceEnum.LASER.getDevice().setLastChange(LocalDateTime.now());
        } else if (getMegatronTestToken().equals(megatronToken)) {
            if(!DeviceEnum.LASER.getDevice().isDeviceReady()){
                throw new MegatronException("device not ready");
            }
            DeviceControlRequest request = new DeviceControlRequest();
            request.setDevice(DeviceEnum.LASER);
            request.setPower(true);
            request.setLevel((short)10);
            request.setPeriod((int) Duration.ofSeconds(5).toSeconds());

            devicesService.sendRequest(request);

        } else {
            throw new MegatronException("Invalid token");
        }
        counter.getAndIncrement();
    }



}
