package ru.sokol.smartoffice.service;

import org.springframework.stereotype.Service;
import ru.sokol.smartoffice.model.DeviceEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DevicesServiceImpl {
    private Map<DeviceEnum,Boolean> switchedDevicesPowerStateMap = new ConcurrentHashMap<>();


}
