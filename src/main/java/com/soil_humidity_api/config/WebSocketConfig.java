package com.soil_humidity_api.config;

import com.soil_humidity_api.handler.EspWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer  {
    private final EspWebSocketHandler espHandler;

    public WebSocketConfig(EspWebSocketHandler espHandler) {
        this.espHandler = espHandler;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // For frontend subscriptions
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Frontend STOMP endpoint (no SockJS needed)
        registry.addEndpoint("/stomp-ws")
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // ESP WebSocket handler
        registry.addHandler(espHandler, "/esp-ws")
                .setAllowedOriginPatterns("*");
    }
}
