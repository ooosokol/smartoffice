package ru.sokol.smartoffice.model.device;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Value
public class SwitchDevice extends AbstractDevice implements Device {
    private static final Duration NOT_READY_TIME = Duration.ofSeconds(5);

    public SwitchDevice(DeviceEnum device) {
        super(device, SwitchDevice.class);
    }

    @Override
    public boolean isDeviceReady() {
        return LocalDateTime.now().isAfter(lastChange.plus(NOT_READY_TIME));
    }}
