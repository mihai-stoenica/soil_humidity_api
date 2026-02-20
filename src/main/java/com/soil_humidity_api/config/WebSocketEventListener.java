package com.soil_humidity_api.config;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

import java.util.Map;

@AllArgsConstructor
@Component
public class WebSocketEventListener {

    private final DeviceSessionRegistry registry;

    @EventListener
    public void handleWebSocketEvent(AbstractSubProtocolEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        // Check what type of event this actually is
        System.out.println("Event Received: " + event.getClass().getSimpleName() + " | Session: " + sessionId);

        if (event instanceof SessionConnectedEvent || event instanceof SessionConnectEvent) {
            Map<String, Object> attrs = accessor.getSessionAttributes();
            if (attrs != null && attrs.containsKey("deviceId")) {
                Long deviceId = Long.valueOf(attrs.get("deviceId").toString());
                registry.register(sessionId, deviceId);
                System.out.println("REGISTRY SUCCESS: " + deviceId);
            }
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

