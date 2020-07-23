package ru.sokol.smartoffice.model.device;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Data
public class FanDevice extends AbstractDevice implements Device {
    Short speed = 0;

    public FanDevice(DeviceEnum device) {
        super(device, FanDevice.class);
        this.power = true;
    }

    @Override
    public boolean isDeviceReady() {
        return true;
    }
}
