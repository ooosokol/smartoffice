package ru.sokol.smartoffice.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sokol.smartoffice.model.device.DeviceEnum;
import ru.sokol.smartoffice.model.device.LaserDevice;
import ru.sokol.smartoffice.model.deviceControlApiModel.DeviceControlRequest;
import ru.sokol.smartoffice.model.deviceControlApiModel.HardwareDeviceEnum;
import ru.sokol.smartoffice.service.exception.MegatronException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MegatronSokolServiceImpl  {
    private static final String secret = "04080F10172A";
    private final AtomicLong counter;
    private LocalDateTime tokenExpireTime;

    private final DevicesServiceImpl devicesService;
    private final FanServiceImpl fanService;

    private final DeviceEnum megatronLaserSwitch = DeviceEnum.SWITCH2;


    public MegatronSokolServiceImpl(DevicesServiceImpl devicesService, FanServiceImpl fanService) {
        this.devicesService = devicesService;
        this.fanService = fanService;
        counter = new AtomicLong(43);
    }


    public String getMegatronTestToken() {
        return DigestUtils.md5Hex(DigestUtils.md5Hex(String.valueOf(counter.get())));
    }

    private String getMegatronToken() {
        return DigestUtils.md5Hex(DigestUtils.md5Hex(counter.get() + secret));
    }

    public boolean getMegatronPowerState(){
        return megatronLaserSwitch.getDevice().getPower() && DeviceEnum.LASER_POWER.getDevice().getPower();
    }



    public synchronized boolean startMegatron(String megatronToken) throws MegatronException {
        if(!DeviceEnum.LASER.getDevice().isDeviceReady()){
            throw new MegatronException("device not ready");
        }
        if (getMegatronToken().equals(megatronToken)) {
            DeviceControlRequest request;
            /*request = new DeviceControlRequest();
            request.setDevice(HardwareDeviceEnum.sw10);
            request.setPower(true);

            devicesService.sendRequest(request);

            request = new DeviceControlRequest();
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
            }*/

            request = new DeviceControlRequest();

            if(fanService.getCurrentPhase() < (short) 5){
                fanService.setCurrentPhase((short)5);
            }

            request.setDevice(HardwareDeviceEnum.pwm1);
            request.setProcess(true);
            request.setPower(true);
            request.setLevel((short)255);
            request.setState(fanService.getCurrentPhase());
            request.setPeriod((int) Duration.ofMinutes(4).toMillis());
            DeviceEnum.LASER.getDevice().setLastChange(LocalDateTime.now());

            devicesService.sendRequest(request);
            try {
                LocalDateTime inactiveTimeEnd = LocalDateTime.now().plus(Duration.ofHours(2));
                Arrays.stream(DeviceEnum.values()).map(DeviceEnum::getDevice).forEach(device -> device.setLastChange(inactiveTimeEnd));
            }catch (Exception e){

            }
            DeviceEnum.LASER.getDevice().setLastChange(LocalDateTime.MAX);

            counter.getAndIncrement();
            return true;



          /*  try {
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
*/



        } else if (getMegatronTestToken().equals(megatronToken)) {



            DeviceControlRequest request;

            /*request = new DeviceControlRequest();
            request.setDevice(HardwareDeviceEnum.sw10);
            request.setPower(true);

            devicesService.sendRequest(request);

            request = new DeviceControlRequest();
            request.setDevice(HardwareDeviceEnum.sw9);
            request.setPower(true);
            request.setPeriod((int) Duration.ofSeconds(1).toMillis());

            devicesService.sendRequest(request);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            request = new DeviceControlRequest();

            if(fanService.getCurrentPhase() < (short) 4){
                fanService.setCurrentPhase((short)4);
            }

            request.setDevice(HardwareDeviceEnum.pwm1);
            request.setPower(true);
            request.setProcess(true);
            request.setLevel((short)10);
            request.setState(fanService.getCurrentPhase());
            request.setPeriod((int) Duration.ofSeconds(5).toMillis());

            devicesService.sendRequest(request);
            DeviceEnum.LASER.getDevice().setLastChange(LocalDateTime.now());
            counter.getAndIncrement();
            return false;


        } else {
            throw new MegatronException("Invalid token");
        }
    }

    @Scheduled(fixedRate = 25000)
    public void counterIncrement(){
        counter.incrementAndGet();
        tokenExpireTime = LocalDateTime.now().plus(Duration.ofMillis(25000));
    }

    public Boolean isReady(){
        return DeviceEnum.LASER.getDevice().isDeviceReady();
    }

    public LocalDateTime getTokenExpireTime(){
        return tokenExpireTime;
    }

}
