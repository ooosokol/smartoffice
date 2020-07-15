package ru.sokol.smartoffice.model.device;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class SwitchDevice extends AbstractDevice implements Device {
    Boolean power = false;

    public SwitchDevice(DeviceEnum device) {
        super(device, DeviceClassEnum.SWITCH);
    }
}
