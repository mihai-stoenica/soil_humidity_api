package com.soil_humidity_api.controller;

import com.soil_humidity_api.dto.DeviceClaimDto;
import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.entity.User;
import com.soil_humidity_api.repository.DeviceRepository;
import com.soil_humidity_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @PostMapping("/claim")
    public ResponseEntity<?> claimDevice(@RequestBody DeviceClaimDto request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        if (deviceRepository.existsByApiKey(request.apiKey())) {
            return ResponseEntity.badRequest().body("Device already claimed by someone!");
        }

        Device device = new Device(request.name(), request.apiKey());
        device.setUser(user);
        deviceRepository.save(device);

        return ResponseEntity.ok().body("Device claimed: " + device.getName());
    }

}
