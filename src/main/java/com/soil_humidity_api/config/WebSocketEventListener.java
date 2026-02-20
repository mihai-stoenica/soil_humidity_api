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
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> attrs = accessor.getSessionAttributes();
        System.out.println("✅✅✅Connection reached endpoint");
        if (attrs != null && attrs.get("deviceId") != null) {
            String sessionId = accessor.getSessionId();
            Long deviceId = Long.valueOf(attrs.get("deviceId").toString());

            registry.register(sessionId, deviceId);
            System.out.println("✅ SUCCESS: Device " + deviceId + " is now ONLINE.");
        }
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        // This will now fire ~20s after you unplug the ESP thanks to heartbeats
        registry.unregister(event.getSessionId());
        System.out.println("❌ DISCONNECT: Session " + event.getSessionId() + " removed.");
    }
}

