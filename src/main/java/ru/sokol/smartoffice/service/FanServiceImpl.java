package ru.sokol.smartoffice.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

    @Scheduled(fixedDelay = 5000)
    public void processNewMonitoringValues() {
        cpuLoad += random.nextInt(14) - 7;
        cpuTemp += random.nextInt(8) - 4;
        fanRpm += random.nextInt(400) - 200;
        long deltaCountFile = Math.abs(captchaService.getCurrentAtomicValue() - lastAtomicValue);
        if (deltaCountFile > 250) {
            cpuLoad += 5 + random.nextInt(5);
            if (cpuLoad > MAX_CPU_LOAD) {
                cpuLoad = MAX_CPU_LOAD;
            }
            cpuTemp += 2 + random.nextInt(4);
            if (cpuTemp > MAX_CPU_TEMP) {
                cpuTemp = MAX_CPU_TEMP;
            }
            fanRpm += 200 + random.nextInt(200);
            if (fanRpm > MAX_FAN_RPM) {
                fanRpm = MAX_FAN_RPM;
            }
        } else if (deltaCountFile < 10) {
            cpuLoad -= random.nextInt(5);
            if (cpuLoad < MIN_CPU_LOAD) {
                cpuLoad = MIN_CPU_LOAD;
            }
            cpuTemp -= random.nextInt(4);
            if (cpuTemp < MIN_CPU_TEMP) {
                cpuTemp = MIN_CPU_TEMP;
            }
            fanRpm -= random.nextInt(200);
            if (fanRpm < MIN_FAN_RPM) {
                fanRpm = MIN_FAN_RPM;
            }
        }
        cpuLoadGauge.set(cpuLoad);
        cpuTempGauge.set(cpuTemp);
        cpuFanRpmGauge.set(fanRpm);

        lastAtomicValue = captchaService.getCurrentAtomicValue();
    }
}
