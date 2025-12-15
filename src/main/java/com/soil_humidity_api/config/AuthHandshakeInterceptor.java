package com.soil_humidity_api.config;

import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.entity.User;
import com.soil_humidity_api.repository.DeviceRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;

public class AuthHandshakeInterceptor implements HandshakeInterceptor {
    private final DeviceRepository deviceRepository;

    public AuthHandshakeInterceptor(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, @NonNull ServerHttpResponse response,
                                   @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {

        String apiKey = request.getHeaders().getFirst("X-API-KEY");

        if(apiKey != null) {
            Optional<Device> deviceOpt = deviceRepository.findByApiKey(apiKey);
            if (deviceOpt.isPresent()) {
                Device device = deviceOpt.get();
                User owner = device.getUser();

                if (owner != null) {
                    attributes.put("userId", String.valueOf(owner.getId()));
                    attributes.put("deviceId", device.getId());
                    return true;
                }
            }
        }

        return request.getPrincipal() != null;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
                               @NonNull WebSocketHandler wsHandler, Exception exception) {}
}
