package com.soil_humidity_api.config;

import com.soil_humidity_api.service.JwtService;
import com.soil_humidity_api.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@AllArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private JwtService jwtService;
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/soil")
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authHeader = accessor.getFirstNativeHeader("Authorization");

                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);

                        try {
                            String username = jwtService.extractUsername(token);

                            if (username != null && jwtService.isTokenValid(token)) {
                                UserDetails user = userDetailsService.loadUserByUsername(username);
                                UsernamePasswordAuthenticationToken auth =
                                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                                accessor.setUser(auth);
                            }
                        } catch (Exception e) {
                            System.err.println("WebSocket JWT Authentication failed: " + e.getMessage());

                        }
                    }
                }
                return message;
            }
        });
    }
}

