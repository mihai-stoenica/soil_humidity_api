package com.soil_humidity_api.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jspecify.annotations.NonNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class EspWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private final SimpMessagingTemplate messagingTemplate;

    public EspWebSocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        // Parse JSON from ESP
        JsonNode node = mapper.readTree(message.getPayload());
        String deviceId = node.get("deviceId").asText();
        int humidity = node.get("humidity").asInt();

        System.out.println("ESP Data received: device=" + deviceId + " humidity=" + humidity);

        // Forward to frontend via STOMP
        messagingTemplate.convertAndSend("/topic/device/" + deviceId + "/humidity", node);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("ESP connected: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        System.out.println("ESP disconnected: " + session.getId());
    }
}
