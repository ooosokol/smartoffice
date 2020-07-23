package ru.sokol.smartoffice.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sokol.smartoffice.model.device.Device;
import ru.sokol.smartoffice.model.device.DeviceEnum;
import ru.sokol.smartoffice.model.device.FanDevice;

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

    private AtomicInteger cpuLoadGauge;
    private AtomicInteger cpuTempGauge;
    private AtomicInteger cpuFanRpmGauge;

    private final CaptchaService captchaService;
    private final MeterRegistry meterRegistry;


    public FanServiceImpl(CaptchaService captchaService, MeterRegistry meterRegistry) {
        this.captchaService = captchaService;
        this.meterRegistry = meterRegistry;
        cpuLoadGauge = meterRegistry.gauge("cpu.load", new AtomicInteger(MIN_CPU_LOAD));
        cpuTempGauge = meterRegistry.gauge("cpu.temp", new AtomicInteger(MIN_CPU_TEMP));
        cpuFanRpmGauge = meterRegistry.gauge("cpu.fan", new AtomicInteger(MIN_FAN_RPM));

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
        FanDevice fanDevice = (FanDevice) DeviceEnum.FAN.getDevice();
        fanDevice.setSpeed((short)  ((Math.max(0, (fanRpm - 2 * MIN_FAN_RPM)) * 100) / (MAX_FAN_RPM - 2 * MIN_FAN_RPM)));
        fanDevice.setLastChange(LocalDateTime.now());
        log.info("change fan device request must be send: {}, fanRpm: {}", fanDevice, fanRpm);
    }
}
