package ru.sokol.smartoffice.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sokol.smartoffice.model.device.DeviceEnum;
import ru.sokol.smartoffice.model.deviceControlApiModel.DeviceControlRequest;
import ru.sokol.smartoffice.model.deviceControlApiModel.HardwareDeviceEnum;
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
            request.setDevice(HardwareDeviceEnum.sw12);
            request.setPower(true);
            request.setPeriod((int) Duration.ofMinutes(3).toMillis());

            devicesService.sendRequest(request);

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            Time.sleep(10000);

            request = new DeviceControlRequest();
            request.setDevice(HardwareDeviceEnum.sw9);
            request.setPower(true);
            request.setPeriod((int) Duration.ofSeconds(2).toMillis());

            devicesService.sendRequest(request);

//            Time.sleep(10000);


            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            request = new DeviceControlRequest();
            request.setDevice(HardwareDeviceEnum.pwm1);
            request.setPower(true);
            request.setLevel((short)255);
            request.setPeriod((int) Duration.ofMinutes(4).toMillis());
            DeviceEnum.LASER.getDevice().setLastChange(LocalDateTime.now());

            devicesService.sendRequest(request);


            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            Time.sleep(10000);

            request = new DeviceControlRequest();
            request.setDevice(HardwareDeviceEnum.sw15);
            request.setPower(true);
            request.setPeriod((int) Duration.ofMinutes(1).toMillis());

            devicesService.sendRequest(request);




        } else if (getMegatronTestToken().equals(megatronToken)) {
            if(!DeviceEnum.LASER.getDevice().isDeviceReady()){
                throw new MegatronException("device not ready");
            }


            DeviceControlRequest request = new DeviceControlRequest();
            request.setDevice(HardwareDeviceEnum.sw9);
            request.setPower(true);
            request.setPeriod((int) Duration.ofSeconds(1).toMillis());

            devicesService.sendRequest(request);

//            Time.sleep(10000);

            request = new DeviceControlRequest();
            request.setDevice(HardwareDeviceEnum.pwm1);
            request.setPower(true);
            request.setLevel((short)10);
            request.setPeriod((int) Duration.ofSeconds(5).toMillis());

            devicesService.sendRequest(request);

        } else {
            throw new MegatronException("Invalid token");
        }
        counter.getAndIncrement();
    }

    @Scheduled(fixedRate = 5000)
    public void counterIncrement(){
        counter.incrementAndGet();
    }



}
