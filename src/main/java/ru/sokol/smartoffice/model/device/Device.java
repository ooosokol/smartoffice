package ru.sokol.smartoffice.model.device;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Device {
    @JsonIgnore
    boolean isDeviceReady();

}
