package ru.sokol.smartoffice.service;

import reactor.core.publisher.Mono;
import ru.sokol.smartoffice.model.device.DeviceControlRequest;
import ru.sokol.smartoffice.model.device.DeviceControlResponse;

public interface DeviceApiClientService {
    Mono<DeviceControlResponse> processRequest(DeviceControlRequest request);
}
