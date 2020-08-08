package ru.sokol.smartoffice.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sokol.smartoffice.model.device.DeviceEnum;
import ru.sokol.smartoffice.model.device.FanDevice;
import ru.sokol.smartoffice.model.deviceControlApiModel.DeviceControlRequest;
import ru.sokol.smartoffice.model.deviceControlApiModel.HardwareDeviceEnum;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class FanServiceImpl {
    private final Integer MIN_CPU_TEMP = 40;
    private final Integer MAX_CPU_TEMP = 94;
    private final Integer MIN_CPU_LOAD = 20;
    private final Integer MAX_CPU_LOAD = 100;
    private final Integer MIN_FAN_RPM = 1000;
    private final Integer MAX_FAN_RPM = 4200;


    private final Random random = new Random();

    private Integer cpuTemp = MIN_CPU_TEMP;
    private Integer cpuLoad = MIN_CPU_LOAD;
    private Integer fanRpm = MIN_FAN_RPM;
    private long lastAtomicValue = 0L;

    private final AtomicInteger cpuLoadGauge;
    private final AtomicInteger cpuTempGauge;
    private final AtomicInteger cpuFanRpmGauge;

    private final CaptchaService captchaService;
    private final DevicesServiceImpl devicesService;


    public FanServiceImpl(CaptchaService captchaService, MeterRegistry meterRegistry, DevicesServiceImpl devicesService) {
        this.captchaService = captchaService;
        cpuLoadGauge = meterRegistry.gauge("cpu.load", new AtomicInteger(MIN_CPU_LOAD));
        cpuTempGauge = meterRegistry.gauge("cpu.temp", new AtomicInteger(MIN_CPU_TEMP));
        cpuFanRpmGauge = meterRegistry.gauge("cpu.fan", new AtomicInteger(MIN_FAN_RPM));
        this.devicesService = devicesService;
    }

    @Scheduled(fixedRate = 5000)
    public synchronized void processNewMonitoringValues() {
        cpuLoad += random.nextInt(14) - 7;
        cpuTemp += random.nextInt(8) - 4;
        fanRpm += random.nextInt(400) - 200;
        long deltaCountFile = Math.abs(captchaService.getCurrentAtomicValue() - lastAtomicValue);
        if (deltaCountFile > 250) {
            cpuLoad += 5 + random.nextInt(5);
            cpuTemp += 2 + random.nextInt(4);
            fanRpm += 200 + random.nextInt(200);
        } else if (deltaCountFile < 10) {
            cpuLoad -= random.nextInt(5);
            cpuTemp -= random.nextInt(4);
            fanRpm -= random.nextInt(200);
        }
        cpuLoad = Math.max(MIN_CPU_LOAD,Math.min(MAX_CPU_LOAD,cpuLoad));
        cpuTemp = Math.max(MIN_CPU_TEMP,Math.min(MAX_CPU_TEMP,cpuTemp));
        fanRpm = Math.max(MIN_FAN_RPM,Math.min(MAX_FAN_RPM,fanRpm));
        cpuLoadGauge.set(cpuLoad);
        cpuTempGauge.set(cpuTemp);
        cpuFanRpmGauge.set(fanRpm);

        lastAtomicValue = captchaService.getCurrentAtomicValue();
        FanDevice fanDevice = (FanDevice) DeviceEnum.FAN1.getDevice();
        fanDevice.setLevel((short)  ((Math.max(0, (fanRpm - 2 * MIN_FAN_RPM)) * 255) / (MAX_FAN_RPM - 2 * MIN_FAN_RPM)));
        fanDevice.setLastChange(LocalDateTime.now());
        DeviceControlRequest fanRequest = new DeviceControlRequest();
        fanRequest.setDevice(HardwareDeviceEnum.ssr3);
        fanRequest.setPower(fanDevice.getLevel() > 30);
        fanRequest.setLevel((short)fanDevice.getLevel() > 30 ? fanDevice.getLevel():0);
        devicesService.sendRequest(fanRequest);

        fanRequest = new DeviceControlRequest();
        fanRequest.setDevice(HardwareDeviceEnum.sw4);
        fanRequest.setPower(fanDevice.getLevel() > 30);
        devicesService.sendRequest(fanRequest);

        fanRequest = new DeviceControlRequest();
        fanRequest.setDevice(HardwareDeviceEnum.ssr2);
        fanRequest.setPower(fanDevice.getLevel() > 180);
        fanRequest.setLevel((short) fanDevice.getLevel() > 180 ? fanDevice.getLevel():0);
        devicesService.sendRequest(fanRequest);


        fanRequest = new DeviceControlRequest();
        fanRequest.setDevice(HardwareDeviceEnum.sw2);
        fanRequest.setPower(fanDevice.getLevel() > 100);
        devicesService.sendRequest(fanRequest);
    }
}
