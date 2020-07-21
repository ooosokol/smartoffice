package ru.sokol.smartoffice.model.device;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.SneakyThrows;

public enum  DeviceEnum {
    SWITCH1(SwitchDevice.class,true, false),
    SWITCH2(SwitchDevice.class, false, true),//Laser switch
    SWITCH3(SwitchDevice.class, true, true),
    LASER(LaserDevice.class, false, true),
    FAN(FanDevice.class, false, true),
    LOGO_LETTER_1(LedDevice.class,true, false),
    LOGO_LETTER_2(LedDevice.class,true, false),
    LOGO_LETTER_3(LedDevice.class,true, false),
    LOGO_LETTER_4(LedDevice.class,true, false),
    LOGO_LETTER_5(LedDevice.class,true, false);


    AbstractDevice device;
    Boolean listed;
    Boolean authorized;


    @SneakyThrows
    DeviceEnum(Class<? extends AbstractDevice> deviceClass, Boolean listed, Boolean authorized) {
        this.device = deviceClass.getConstructor(DeviceEnum.class).newInstance(this);
        this.listed = listed;
        this.authorized = authorized;
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
