package com.soil_humidity_api.controller;

import com.soil_humidity_api.dto.request.MqttAuthRequestDto;
import com.soil_humidity_api.repository.DeviceRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@RestController
@RequestMapping("/api/mqtt")
@RequiredArgsConstructor
public class MqttController {
    private final DeviceRepository deviceRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "/auth")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody MqttAuthRequestDto request,
            @Value("${MQTT_USER}") String user,
            @Value("${MQTT_PWD}") String password
    ) {
        if(request.username().equals(user) && request.password().equals(password))
            return ResponseEntity.ok().body(Map.of("result","allow"));

        boolean isValid = deviceRepository.findByApiKey(request.username())
                .map(device -> passwordEncoder.matches(request.password(), device.getSecret()))
                .orElse(false);

        return isValid ? ResponseEntity.ok().body(Map.of("result","allow")) : ResponseEntity.ok().body(Map.of("result","deny"));
    }

}
