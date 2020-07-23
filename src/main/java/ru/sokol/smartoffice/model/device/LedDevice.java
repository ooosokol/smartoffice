package ru.sokol.smartoffice.model.device;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Value
public class LedDevice extends AbstractDevice implements Device {
    String color = "000000";
    private static final Duration NOT_READY_TIME = Duration.ofSeconds(5);

    public LedDevice(DeviceEnum device) {
        super(device, LedDevice.class);
    }

    @Override
    public boolean isDeviceReady() {
        return LocalDateTime.now().isAfter(lastChange.plus(NOT_READY_TIME));
    }
}
