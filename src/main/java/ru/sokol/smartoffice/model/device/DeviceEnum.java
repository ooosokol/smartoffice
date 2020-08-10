package ru.sokol.smartoffice.model.device;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.SneakyThrows;
import ru.sokol.smartoffice.model.deviceControlApiModel.HardwareDeviceEnum;

public enum  DeviceEnum {
    SWITCH1(SwitchDevice.class,true, false,"5eb55c8069be7a0cffb193a8",HardwareDeviceEnum.sw11),
    SWITCH2(SwitchDevice.class, false, true, "5eb55c8069be7a0cffb193a9", HardwareDeviceEnum.sw5),//Laser switch
    LASER_POWER(SwitchDevice.class, false, false, "85096850-9688-4ccc-a96e-6da9647834b285096850-9688-4ccc-a96e-6da9647834b2", HardwareDeviceEnum.sw10),//Laser switch
    SWITCH3(SwitchDevice.class, true, true, "5eb55c8069be7a0cffb193aa", HardwareDeviceEnum.sw13),
    SWITCH4(SwitchDevice.class, true, false,"5eb55c8069be7a0cffb193ab",HardwareDeviceEnum.sw7),
    SWITCH5(SwitchDevice.class, true, false,"5eb55c8069be7a0cffb193ac",HardwareDeviceEnum.sw8),
    LASER(LaserDevice.class, false, true, HardwareDeviceEnum.pwm1),
    FAN1(FanDevice.class, false, true,HardwareDeviceEnum.ssr3),
    FAN2(FanDevice.class, false, true,HardwareDeviceEnum.ssr2),
    LOGO_LETTER_1(LedDevice.class,true, false,"5eb55c8069be7a0cffb193ad", (short)456,(short) 587,(short)20),
    LOGO_LETTER_2(LedDevice.class,true, false,"5eb55c8069be7a0cffb193ae", (short)337,(short) 456,(short)35),
    LOGO_LETTER_3(LedDevice.class,true, false,"5eb55c8069be7a0cffb193af", (short)211,(short) 337,(short)50),
    LOGO_LETTER_4(LedDevice.class,true, false,"5eb55c8069be7a0cffb193b0", (short)111,(short) 211,(short)35),
    LOGO_LETTER_5(LedDevice.class,true, false, "5eb55c8069be7a0cffb193b1", (short)0,(short) 111,(short)20);


    String externalIdent;
    AbstractDevice device;
    Boolean listed;
    Boolean authorized;
    HardwareDeviceEnum hardwareDevice;
    Short start;
    Short end;
    Short level;


    @SneakyThrows
    DeviceEnum(Class<? extends AbstractDevice> deviceClass, Boolean listed, Boolean authorized, String externalIdent, HardwareDeviceEnum hardwareDevice) {
        this.device = deviceClass.getConstructor(DeviceEnum.class).newInstance(this);
        this.listed = listed;
        this.authorized = authorized;
        this.externalIdent = externalIdent;
        this.hardwareDevice = hardwareDevice;
    }
    @SneakyThrows
    DeviceEnum(Class<? extends AbstractDevice> deviceClass, Boolean listed, Boolean authorized, String externalIdent, Short start, Short end, Short level) {
        this.device = deviceClass.getConstructor(DeviceEnum.class).newInstance(this);
        this.listed = listed;
        this.authorized = authorized;
        this.externalIdent = externalIdent;
        this.hardwareDevice = HardwareDeviceEnum.ws2813;
        this.start = start;
        this.end = end;
        this.level = level;
    }
@SneakyThrows
    DeviceEnum(Class<? extends AbstractDevice> deviceClass, Boolean listed, Boolean authorized, HardwareDeviceEnum hardwareDevice) {
        this.device = deviceClass.getConstructor(DeviceEnum.class).newInstance(this);
        this.listed = listed;
        this.authorized = authorized;
        this.hardwareDevice = hardwareDevice;
    }

    public AbstractDevice getDevice() {
        return device;
    }

    public Boolean getListed() {
        return listed;
    }

    public Boolean getAuthorized() {
        return authorized;
    }

    public HardwareDeviceEnum getHardwareDevice() {
        return hardwareDevice;
    }

    public Short getStart() {
        return start;
    }

    public Short getEnd() {
        return end;
    }

    public Short getLevel() {
        return level;
    }

    @JsonValue
    public String getIdentifier(){
        return this.externalIdent;
    }

    @SuppressWarnings("unused")
    @JsonCreator
    public static DeviceEnum forValue(String externalIdent) {
        for (DeviceEnum device : DeviceEnum.values()) {
            if (device.getIdentifier() != null  && device.getIdentifier().equals(externalIdent)) {
                return device;
            }
        }
        return null;
    }
}
