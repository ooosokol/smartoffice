package ru.sokol.smartoffice.model.device;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class LedDevice extends AbstractDevice implements Device {
    String color = "DEADBE";
    private static final Duration NOT_READY_TIME = Duration.ofSeconds(5);

    public LedDevice(DeviceEnum device) {
        super(device, LedDevice.class);
    }

    @Override
    public boolean isDeviceReady() {
        return LocalDateTime.now().isAfter(lastChange.plus(NOT_READY_TIME));
    }
}
