package ru.sokol.smartoffice.model.device;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.SneakyThrows;

public enum  DeviceEnum {
    SWITCH1(SwitchDevice.class,true),
    SWITCH2(SwitchDevice.class, true),
    SWITCH3(SwitchDevice.class, true),
    LASER(LaserDevice.class, false),
    FAN(FanDevice.class, false),
    LOGO_LETTER_1(LedDevice.class,true),
    LOGO_LETTER_2(LedDevice.class,true),
    LOGO_LETTER_3(LedDevice.class,true),
    LOGO_LETTER_4(LedDevice.class,true),
    LOGO_LETTER_5(LedDevice.class,true);


    AbstractDevice device;
    Boolean listed;


    @SneakyThrows
    DeviceEnum(Class<? extends AbstractDevice> deviceClass, Boolean listed) {
        this.device = deviceClass.getConstructor(DeviceEnum.class).newInstance(this);
        this.listed = listed;
    }

    public AbstractDevice getDevice() {
        return device;
    }

    public Boolean getListed() {
        return listed;
    }

    @JsonValue
    public String getIdentifier(){
        return this.name();
    }

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
