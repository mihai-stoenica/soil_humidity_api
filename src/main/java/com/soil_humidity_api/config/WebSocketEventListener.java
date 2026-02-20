package com.soil_humidity_api.config;

import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.repository.DeviceRepository;
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
    private final DeviceRepository deviceRepository;

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> attrs = accessor.getSessionAttributes();

        Long deviceId = null;

        // Plan A: Check Session Attributes (from Interceptor)
        if (attrs != null && attrs.get("deviceId") != null) {
            deviceId = Long.valueOf(attrs.get("deviceId").toString());
        }
        // Plan B: Check STOMP Headers (from the CONNECT frame)
        else {
            String apiKey = accessor.getFirstNativeHeader("X-API-KEY");
            System.out.println("first header: " + apiKey);
            if (apiKey != null) {
                // You'll need to inject your repository here if not already available
                deviceId = deviceRepository.findByApiKey(apiKey)
                        .map(Device::getId)
                        .orElse(null);
            }
        }

        if (deviceId != null) {
            registry.register(accessor.getSessionId(), deviceId);
            System.out.println("✅ SUCCESS: Device " + deviceId + " registered via " +
                    (attrs != null && attrs.get("deviceId") != null ? "Attributes" : "STOMP Header"));
        } else {
            System.out.println("❌ FAILED: No Device ID found in Attributes or STOMP Headers");
        }
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        // This will now fire ~20s after you unplug the ESP thanks to heartbeats
        registry.unregister(event.getSessionId());
        System.out.println("❌ DISCONNECT: Session " + event.getSessionId() + " removed.");
    }
}

