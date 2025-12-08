package com.soil_humidity_api.config;

import com.soil_humidity_api.repository.DeviceRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final DeviceRepository deviceRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Try Header (For REST API)
        String apiKey = request.getHeader("X-API-KEY");

        // 2. Try URL Parameter (For WebSockets)
        if (apiKey == null) {
            apiKey = request.getParameter("apiKey");
        }

        // DEBUG LOGGING (Check your Docker console!)
        if (request.getRequestURI().startsWith("/ws/")) {
            System.out.println("WS Handshake detected. Path: " + request.getRequestURI());
            System.out.println("Found API Key: " + apiKey);
        }

        // 3. Authenticate if Key Found
        if (apiKey != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            var device = deviceRepository.findByApiKey(apiKey).orElse(null);

            if (device != null) {
                System.out.println("Device Authenticated: " + device.getName());

                UserDetails userDetails = new User(device.getName(), "",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_DEVICE")));

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                auth.setDetails(device);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                System.out.println("API Key not found in DB!");
            }
        }

        filterChain.doFilter(request, response);
    }
}