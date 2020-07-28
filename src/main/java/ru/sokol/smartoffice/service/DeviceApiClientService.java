package ru.sokol.smartoffice.service;

import reactor.core.publisher.Mono;
import ru.sokol.smartoffice.model.deviceControlApiModel.DeviceControlRequest;
import ru.sokol.smartoffice.model.deviceControlApiModel.DeviceControlResponse;

public interface DeviceApiClientService {
    Mono<DeviceControlResponse> processRequest(DeviceControlRequest request);
}
