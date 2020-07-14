package ru.sokol.smartoffice.service;

import reactor.core.publisher.Mono;
import ru.sokol.smartoffice.model.DeviceControlRequest;
import ru.sokol.smartoffice.model.DeviceControlResponse;

public interface DeviceApiClientService {
    Mono<DeviceControlResponse> processRequest(DeviceControlRequest request);
}
