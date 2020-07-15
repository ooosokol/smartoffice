package ru.sokol.smartoffice.model.device;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class LaserDevice extends AbstractDevice implements Device {
    Short level = 0;
    Integer period = 0;

    public LaserDevice(DeviceEnum device) {
        super(device, DeviceClassEnum.LASER);
    }
}
