package com.soil_humidity_api.config;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeviceSessionRegistry {

    private final Map<String, Long> sessionIdToDeviceId = new ConcurrentHashMap<>();
    private final Map<Long, String> deviceIdToSessionId = new ConcurrentHashMap<>();

    public void register(String sessionId, Long deviceId) {
        sessionIdToDeviceId.put(sessionId, deviceId);
        deviceIdToSessionId.put(deviceId, sessionId);
    }

    public void unregister(String sessionId) {
        Long deviceId = sessionIdToDeviceId.remove(sessionId);
        if (deviceId != null) {
            deviceIdToSessionId.remove(deviceId, sessionId);
        }
    }

    public boolean isDeviceConnected(Long deviceId) {
        return deviceIdToSessionId.containsKey(deviceId);
    }

    public Set<Long> getConnectedDeviceIds() {
        return Set.copyOf(deviceIdToSessionId.keySet());
    }
}

