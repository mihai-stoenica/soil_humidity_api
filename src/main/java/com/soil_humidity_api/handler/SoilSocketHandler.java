package com.soil_humidity_api.handler;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SoilSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> deviceSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        System.out.println("Device Registered");

    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {

        System.out.println("Device Disconnected");
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) {
        System.out.println("Received message: " + message.getPayload());
    }

    private String extractApiKey(String query) {
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && "apiKey".equals(pair[0])) {
                return pair[1];
            }
        }
        return null;
    }

}
