package ru.sokol.smartoffice.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class DeviceWebSocketHandler implements WebSocketHandler {
    @Override
    public @NotNull Mono<Void> handle(WebSocketSession session) {
        return session
                .send(session.receive()
                        .map(msg -> "RECEIVED ON SERVER :: " + msg.getPayloadAsText())
                        .map(session::textMessage)
                );
    }
}
