package ru.sokol.smartoffice.model.device;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Value
public class LaserDevice extends AbstractDevice implements Device {
    Short level = 0;
    Integer period = 0;
    private static final Duration NOT_READY_TIME = Duration.ofMinutes(1);


    public LaserDevice(DeviceEnum device) {
        super(device, LaserDevice.class);
    }

    @Override
    public boolean isDeviceReady() {
        return this.lastChange.plus(NOT_READY_TIME).isBefore(LocalDateTime.now());
    }
}
