package ru.sokol.smartoffice.model.device;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class AbstractDevice implements Device {
    private final DeviceEnum device;
    @JsonIgnore
    private final Class<? extends AbstractDevice> deviceClass;
    Boolean power = false;
    @JsonIgnore
    LocalDateTime lastChange = LocalDateTime.MIN;

    public AbstractDevice(DeviceEnum device, Class<? extends AbstractDevice> deviceClass) {
        this.device = device;
        this.deviceClass = deviceClass;

    }

    public AbstractDevice() {
        device = null;
        deviceClass = null;
    }
}
