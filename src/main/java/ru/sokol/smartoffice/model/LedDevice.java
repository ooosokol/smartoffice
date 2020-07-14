package ru.sokol.smartoffice.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class LedDevice extends AbstractDevice implements Device {
    Boolean power = false;
    String color = "000000";

    public LedDevice(DeviceEnum device) {
        super(device, DeviceClassEnum.LED);
    }
}
