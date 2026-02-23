package com.soil_humidity_api.service;

import com.soil_humidity_api.repository.DeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class DeviceStatusService {
    private final DeviceRepository deviceRepository;

    public void handleStatusUpdate(String topic, String status) {
        try {
            String[] parts = topic.split("/");
            String deviceKey = parts[2];

            deviceRepository.findByApiKey(deviceKey).ifPresent(device -> {
                boolean isOnline = "online".equalsIgnoreCase(status);

                device.setConnected(isOnline);
                device.setLastSeen(Instant.now());

                deviceRepository.save(device);

                System.out.println("Device " + deviceKey + " is now " + status);
            });
        } catch (Exception e) {
            System.err.println("Error processing status: " + e.getMessage());
        }
    }

}
