package ru.sokol.smartoffice.service;

import reactor.core.publisher.Mono;

public interface CaptchaService {
    Mono<byte[]> getNextCaptcha();
}
