package com.soil_humidity_api.config;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@AllArgsConstructor
@Component
public class WebSocketEventListener {

    private final DeviceSessionRegistry registry;

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        // 1. Wrap the message from the event
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        // 2. Get attributes from the accessor
        Map<String, Object> attrs = accessor.getSessionAttributes();

        if (attrs != null && attrs.containsKey("deviceId")) {
            String sessionId = accessor.getSessionId();
            // Be careful with the cast! If it's stored as Integer, (Long) will fail silently
            Long deviceId = Long.valueOf(attrs.get("deviceId").toString());

            registry.register(sessionId, deviceId);
            System.out.println("SUCCESS: Device " + deviceId + " registered on Session " + sessionId);
        } else {
            // Log this to see if attributes are missing
            System.out.println("WARNING: Connection established but deviceId missing in attributes.");
        }

    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        registry.unregister(sessionId);
        System.out.println("Unregistered Session: " + sessionId);
    }
}

