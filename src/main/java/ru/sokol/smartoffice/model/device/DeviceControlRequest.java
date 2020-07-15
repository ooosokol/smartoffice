package ru.sokol.smartoffice.model.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
public class DeviceControlRequest {
    @JsonProperty("deviceIdentifier")
    DeviceEnum device;

    Boolean power;

    @Positive
    @Max(value = 100)
    Short speed;

    @Pattern(regexp = "^[A-Fa-f0-9]{6}$")
    String color;

    @Positive
    Integer period;

    @Positive
    @Max(value = 100)
    Short level;



}
