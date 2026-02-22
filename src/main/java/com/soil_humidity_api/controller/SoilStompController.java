package com.soil_humidity_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soil_humidity_api.dto.ws.ContinuousSensorCommandDto;
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
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class SoilStompController {
    private final DeviceRepository deviceRepository;
    private final ContinuousCommandMapper continuousCommandMapper;
    private final StepCommandMapper stepCommandMapper;
    private final IMqttClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MessageMapping("/user")
    @SendTo("/topic/user")
    public void handleUserMessage(@Valid @Payload UserDataDto payload, Principal principal) {

        String email = principal.getName();
        Long deviceId = payload.deviceId();

        if (deviceId == null) {
            return;
        }

        Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
        Device device;
        Preset preset;
        if(deviceOpt.isPresent()) {
            device = deviceOpt.get();

            if(!device.getUser().getEmail().equals(email)) {
                return;
            }

            preset = device.getActivePreset();
        } else {
            return;
        }

        if(preset.getPattern() == Pattern.CONTINUOUS) {
            ContinuousSensorCommandDto response = continuousCommandMapper.toDto(preset, payload);

            try {
                String json = objectMapper.writeValueAsString(response);
                client.publish(
                        "topic/user/" + device.getApiKey(),
                        new MqttMessage(json.getBytes())
                );
            } catch (Exception e) {
               System.out.println("Error while sending the command.");
            }

        } else if(preset.getPattern() == Pattern.STEP) {
            StepSensorCommandDto response = stepCommandMapper.toDto(preset, payload);

            try {
                String json = objectMapper.writeValueAsString(response);
                client.publish(
                        "topic/user/" + device.getApiKey(),
                        new MqttMessage(json.getBytes())
                );
            } catch (Exception e) {
                System.out.println("Error while sending the command.");
            }
        }



    }
}

