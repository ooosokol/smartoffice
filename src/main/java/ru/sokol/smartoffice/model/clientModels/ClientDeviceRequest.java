package ru.sokol.smartoffice.model.clientModels;

import lombok.Value;
import ru.sokol.smartoffice.model.device.DeviceEnum;

import javax.validation.constraints.Pattern;

@Value
public class ClientDeviceRequest {
    DeviceEnum device;
    Boolean power;
    @Pattern(regexp = "^[A-Fa-f0-9]{6}$")
    String color;

    String login;
    String password;

    String action;

}
