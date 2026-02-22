package com.soil_humidity_api.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Component
public class WebSocketEventListener {

    private final DeviceSessionRegistry registry;

    public WebSocketEventListener(DeviceSessionRegistry registry) {
        this.registry = registry;
    }

    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> attrs = accessor.getSessionAttributes();
        if (attrs == null) return;

        String sessionId = accessor.getSessionId();
        Long deviceId = (Long) attrs.get("deviceId") ;

        if (deviceId != null) {
            registry.register(sessionId, deviceId);
            System.out.println("Device " + deviceId + " connected (session " + sessionId + ")");
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


