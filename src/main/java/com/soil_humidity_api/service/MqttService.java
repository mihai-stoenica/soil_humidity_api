package com.soil_humidity_api.service;

import com.soil_humidity_api.dto.ws.SensorDataDto;
import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.repository.DeviceRepository;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Instant;
import java.util.Optional;

@Service
public class MqttService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DeviceRepository deviceRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MqttService(
            IMqttClient mqttClient,
            DeviceRepository deviceRepository,
            SimpMessagingTemplate messagingTemplate
    ) throws MqttException {
        this.deviceRepository = deviceRepository;
        this.messagingTemplate = messagingTemplate;

        mqttClient.setCallback(new MqttCallback()
        {
            @Override public void connectionLost(Throwable cause) {
                System.out.println("Connection to MQTT broker lost!");
            }

            @Override public void messageArrived(String topic, MqttMessage message) {
                try {
                    String payload = new String(message.getPayload());

                    SensorDataDto data = objectMapper.readValue(payload, SensorDataDto.class);

                    System.out.println("Humidity: " + data.humidity());
                    System.out.println("Temp: " + data.temperature());

                    handleSensorData(topic, data);

                } catch (Exception e) {
                    System.err.println("Invalid MQTT payload: " + e.getMessage());
                }
            }

            @Override public void deliveryComplete(IMqttDeliveryToken token)
            {  }
        });
        mqttClient.subscribe("soil/device/+/telemetry"); }


    private void handleSensorData(String topic, SensorDataDto payload) {
        String[] parts = topic.split("/");

        if (parts.length > 2) {
            String deviceIdStr = parts[2];

            try {
                Long deviceId = Long.parseLong(deviceIdStr);

                Optional<Device> optionalDevice = deviceRepository.findById(deviceId);

                optionalDevice.ifPresent(device -> {
                    device.setLastHumidity(payload.humidity());
                    device.setLastTemperature(payload.temperature());
                    device.setLastSeen(Instant.now());

                    deviceRepository.save(device);
                });

                messagingTemplate.convertAndSend("/topic/device/" + deviceIdStr, payload);

            } catch (NumberFormatException e) {
                System.out.println("Error while parsing number");
            }

        }
    }
}
