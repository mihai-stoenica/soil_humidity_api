package com.soil_humidity_api.config;

import com.soil_humidity_api.handler.SoilSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final SoilSocketHandler soilSocketHandler;

    public WebSocketConfig(SoilSocketHandler soilSocketHandler) {
        this.soilSocketHandler = soilSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(soilSocketHandler, "/ws/soil").setAllowedOrigins("*");
    }
}
