package ru.sokol.smartoffice.model.deviceControlApiModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import ru.sokol.smartoffice.model.device.DeviceEnum;

public enum HardwareDeviceEnum {
    sw1,
    sw2,
    sw3,
    sw4,
    sw5,
    sw6,
    sw7,
    sw8,
    sw9,
    sw10,
    sw11,
    sw12,
    sw13,
    sw14,
    sw15,
    sw16,
    sw17,
    sw18,
    ssr1,
    ssr2,
    ssr3,
    ssr4,
    pwm1,
    ws2813;

    @JsonValue
    public String getIdentifier(){
        return this.name();
    }

    @SuppressWarnings("unused")
    @JsonCreator
    public static DeviceEnum forValue(String name) {
        for (DeviceEnum device : DeviceEnum.values()) {
            if (device.name().equals(name)) {
                return device;
            }
        }
        return null;
    }
}
