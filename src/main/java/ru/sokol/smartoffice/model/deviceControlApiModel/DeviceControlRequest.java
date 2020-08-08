package ru.sokol.smartoffice.model.deviceControlApiModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
public class DeviceControlRequest implements Serializable {
    @JsonProperty("deviceIdentifier")
    HardwareDeviceEnum device;

    Boolean power;

    @Pattern(regexp = "^[A-Fa-f0-9]{6}$")
    String color;

    @Positive
    Integer period;

    @Positive
    @Max(value = 255)
    Short level;

    @Positive
    Short end;

    Short start;





}
