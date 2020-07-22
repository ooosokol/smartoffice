package ru.sokol.smartoffice.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.sokol.smartoffice.service.WebSocketServiceImpl;

import java.io.IOException;

@Component
@Slf4j
public class DeviceWebSocketHandler extends TextWebSocketHandler {
    private final WebSocketServiceImpl webSocketService;

    public DeviceWebSocketHandler(WebSocketServiceImpl webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage messageText) throws IOException {
        session.sendMessage(messageText);
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        webSocketService.addClient(session);
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        webSocketService.dropClient(session);
    }


}
