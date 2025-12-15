package com.soil_humidity_api.controller;

import com.soil_humidity_api.dto.ws.SensorDataDto;
import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.repository.DeviceRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@RestController
public class SoilStompController {
    private final SimpMessagingTemplate messagingTemplate;
    private final DeviceRepository deviceRepository;

    public SoilStompController(SimpMessagingTemplate messagingTemplate, DeviceRepository deviceRepository) {
        this.messagingTemplate = messagingTemplate;
        this.deviceRepository = deviceRepository;
    }

    @MessageMapping("/device")
    @SendTo("/topic/device")
    public void handleDeviceMessage(@Payload SensorDataDto payload, SimpMessageHeaderAccessor headerAccessor) {

        if (payload.humidity() == null) {
            System.err.println("Received empty humidity data");
            return;
        }
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

        assert sessionAttributes != null;
        String ownerUserId = (String) sessionAttributes.get("userId");

        Long deviceId = (Long) sessionAttributes.get("deviceId");

        if (ownerUserId == null || deviceId == null) {
            System.out.println("Error: Missing User ID or DeviceId found in session. Interceptor might have failed.");
            return;
        }

        Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
        if(deviceOpt.isPresent()) {
            Device device = deviceOpt.get();

            device.setLastHumidity(payload.humidity());
            device.setLastSeen(Instant.now());

            deviceRepository.save(device);
        }

        System.out.println("Routing data to User ID: " + ownerUserId);

        messagingTemplate.convertAndSend("/topic/device/" + ownerUserId, payload);
    }
}

