package com.soil_humidity_api.controller;

import com.soil_humidity_api.dto.ws.ContinuousSensorCommandDto;
import com.soil_humidity_api.dto.ws.SensorDataDto;
import com.soil_humidity_api.dto.ws.StepSensorCommandDto;
import com.soil_humidity_api.dto.ws.UserDataDto;
import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.entity.Preset;
import com.soil_humidity_api.enums.Pattern;
import com.soil_humidity_api.mapper.ContinuousCommandMapper;
import com.soil_humidity_api.mapper.StepCommandMapper;
import com.soil_humidity_api.repository.DeviceRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class SoilStompController {
    private final SimpMessagingTemplate messagingTemplate;
    private final DeviceRepository deviceRepository;
    private final ContinuousCommandMapper continuousCommandMapper;
    private final StepCommandMapper stepCommandMapper;

    @MessageMapping("/device")
    @SendTo("/topic/device")
    public void handleDeviceMessage(@Valid @Payload SensorDataDto payload, SimpMessageHeaderAccessor headerAccessor) {

        if (payload.humidity() == null || payload.temperature() == null) {
            System.err.println("Received empty humidity data");
            return;
        }
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

        assert sessionAttributes != null;

        Long deviceId = (Long) sessionAttributes.get("deviceId");

        if (deviceId == null) {
            System.out.println("Error: Missing DeviceId found in session. Interceptor might have failed.");
            return;
        }

        Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
        if(deviceOpt.isPresent()) {
            Device device = deviceOpt.get();

            device.setLastHumidity(payload.humidity());
            device.setLastSeen(Instant.now());

            deviceRepository.save(device);
        }

        messagingTemplate.convertAndSend("/topic/device/" + deviceId, payload);
    }

    @MessageMapping("/user")
    @SendTo("/topic/user")
    public void handleUserMessage(@Valid @Payload UserDataDto payload) {

        Long deviceId = payload.deviceId();

        if (deviceId == null) {
            System.out.println("Error: Missing DeviceId found in session. Interceptor might have failed.");
            return;
        }
        Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
        Device device;
        Preset preset;
        if(deviceOpt.isPresent()) {
            device = deviceOpt.get();
            preset = device.getActivePreset();
        } else {
            System.out.println("Error: Device does not exist.");
            return;
        }

        if(preset.getPattern() == Pattern.CONTINUOUS) {
            ContinuousSensorCommandDto response = continuousCommandMapper.toDto(preset, payload);
            messagingTemplate.convertAndSend("/topic/user/" + device.getApiKey(), response);
        } else if(preset.getPattern() == Pattern.STEP) {
            StepSensorCommandDto response = stepCommandMapper.toDto(preset, payload);
            messagingTemplate.convertAndSend("/topic/user/" + device.getApiKey(), response);
        }



    }
}

