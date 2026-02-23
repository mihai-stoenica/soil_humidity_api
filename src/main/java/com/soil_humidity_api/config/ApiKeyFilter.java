package com.soil_humidity_api.config;

import com.soil_humidity_api.repository.DeviceRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final DeviceRepository deviceRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader("X-API-KEY");
        String rawSecret = request.getHeader("X-SECRET");


        if (apiKey != null && rawSecret != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            deviceRepository.findByApiKey(apiKey).ifPresent(device -> {
                if (passwordEncoder.matches(rawSecret, device.getSecret())) {

                    UserDetails deviceDetails = User.withUsername(device.getApiKey())
                            .password("")
                            .authorities("ROLE_DEVICE")
                            .build();

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            deviceDetails, null, deviceDetails.getAuthorities());

                    auth.setDetails(device);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            });

        }

        filterChain.doFilter(request, response);
    }
}