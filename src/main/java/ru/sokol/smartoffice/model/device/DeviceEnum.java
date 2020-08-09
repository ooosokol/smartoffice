package ru.sokol.smartoffice.model.device;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.SneakyThrows;
import ru.sokol.smartoffice.model.deviceControlApiModel.HardwareDeviceEnum;

public enum  DeviceEnum {
    SWITCH1(SwitchDevice.class,true, false,"c26f109d-33ce-4287-abe7-b114828f4a47",HardwareDeviceEnum.sw11),
    SWITCH2(SwitchDevice.class, false, true, "85096850-9688-4ccc-a96e-6da9647834b2", HardwareDeviceEnum.sw5),//Laser switch
    LASER_POWER(SwitchDevice.class, false, false, "85096850-9688-4ccc-a96e-6da9647834b285096850-9688-4ccc-a96e-6da9647834b2", HardwareDeviceEnum.sw10),//Laser switch
    SWITCH3(SwitchDevice.class, true, true, "58e8d2ce-2c54-40d0-ab6b-a9843cb11979", HardwareDeviceEnum.sw13),
    SWITCH4(SwitchDevice.class, true, false,"e716033e-e371-42e5-a0de-802c46f558cc",HardwareDeviceEnum.sw7),
    SWITCH5(SwitchDevice.class, true, false,"dd170f02-6f78-44ae-bf53-056d61b1c4b4",HardwareDeviceEnum.sw8),
    LASER(LaserDevice.class, false, true, HardwareDeviceEnum.pwm1),
    FAN1(FanDevice.class, false, true,HardwareDeviceEnum.ssr3),
    FAN2(FanDevice.class, false, true,HardwareDeviceEnum.ssr2),
    LOGO_LETTER_1(LedDevice.class,true, false,"81a2e1ee-4b18-4364-ab59-2f6ef1140efa", (short)456,(short) 587,(short)20),
    LOGO_LETTER_2(LedDevice.class,true, false,"e0be0e83-79e3-419b-8381-edea7806d377", (short)337,(short) 456,(short)35),
    LOGO_LETTER_3(LedDevice.class,true, false,"850d68ab-7235-4ae6-8f6e-cdbeb88df8c2", (short)211,(short) 337,(short)50),
    LOGO_LETTER_4(LedDevice.class,true, false,"1a27947b-601a-42ec-9bf5-81c5106ef3c9", (short)111,(short) 211,(short)35),
    LOGO_LETTER_5(LedDevice.class,true, false, "fd973a42-4a63-4a9d-8f2f-fa9b994df487", (short)0,(short) 111,(short)20);


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
