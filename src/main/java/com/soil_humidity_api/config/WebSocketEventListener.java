package com.soil_humidity_api.config;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@AllArgsConstructor
@Component
public class WebSocketEventListener {

    private final DeviceSessionRegistry registry;

    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> attrs = accessor.getSessionAttributes();

        if (attrs != null && attrs.get("deviceId") != null) {
            try {
                Long deviceId = Long.valueOf(attrs.get("deviceId").toString());
                String sessionId = accessor.getSessionId();

                registry.register(sessionId, deviceId);
                System.out.println("✅ REAL DEVICE REGISTERED: " + deviceId);
            } catch (Exception e) {
                System.out.println("⚠️ Found deviceId but could not parse: " + attrs.get("deviceId"));
            }
        } else {
            // This is where your Phantom/Probes end up
            System.out.println("👻 Ignoring Phantom/Anonymous Connection: " + accessor.getSessionId());
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

