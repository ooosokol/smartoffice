package ru.sokol.smartoffice.model;

import lombok.Data;

@Data
public class AbstractDevice implements Device {
    private final DeviceEnum device;
    private final DeviceClassEnum deviceClass;

    public AbstractDevice(DeviceEnum device, DeviceClassEnum deviceClass) {
        this.device = device;
        this.deviceClass = deviceClass;

    }

}
