package ru.sokol.smartoffice.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class FanDevice extends AbstractDevice implements Device {
    Short speed = 0;

    public FanDevice(DeviceEnum device) {
        super(device, DeviceClassEnum.FAN);
    }
}
