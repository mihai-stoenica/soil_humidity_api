package com.soil_humidity_api.controller;

import com.soil_humidity_api.config.DeviceSessionRegistry;
import com.soil_humidity_api.dto.request.DeviceClaimDto;
import com.soil_humidity_api.dto.response.DeviceDto;
import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.entity.User;
import com.soil_humidity_api.repository.DeviceRepository;
import com.soil_humidity_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final DeviceSessionRegistry deviceSessionRegistry;

    @PostMapping("/claim")
    public ResponseEntity<?> claimDevice(@RequestBody DeviceClaimDto request) {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        if (deviceRepository.existsByApiKey(request.apiKey())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Device already claimed by someone!"));
        }

        Device device = new Device(request.name(), request.apiKey());
        device.setUser(user);
        deviceRepository.save(device);

        DeviceDto deviceDto = new DeviceDto(device.getId(), device.getName(), deviceSessionRegistry.isDeviceConnected(device.getId()), device.getLastSeen(), device.getLastHumidity());

        return ResponseEntity.ok(deviceDto);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        User user = userRepository.findByEmail(email).orElseThrow();

        List<DeviceDto> devices = user.getDevices()
                .stream()
                .map(d -> new DeviceDto(d.getId(), d.getName(), deviceSessionRegistry.isDeviceConnected(d.getId()), d.getLastSeen(), d.getLastHumidity()))
                .toList();

        return ResponseEntity.ok(devices);
    }



}
