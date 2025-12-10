package com.soil_humidity_api.handler;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
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
        /*String query = session.getUri().getQuery();
        String apiKey = extractApiKey(query);

        if(apiKey != null) {
            deviceSessions.put(apiKey, session);
            System.out.println("Device Registered: " + apiKey);
        } else {
            session.close(CloseStatus.BAD_DATA);
        }*/

    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {

        System.out.println("Device Disconnected");
        /*String query = session.getUri().getQuery();
        String apiKey = extractApiKey(query);
        if (apiKey != null) {
            deviceSessions.remove(apiKey);
            System.out.println("Device Disconnected: " + apiKey);
        }*/
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) {
        if (message instanceof TextMessage textMessage) {
            System.out.println("Received message: " + textMessage.getPayload());
        } else {
            System.out.println("Received non-text message: " + message);
        }
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
