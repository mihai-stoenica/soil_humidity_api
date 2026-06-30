package com.soil_humidity_api.service;

import com.soil_humidity_api.dto.ws.SensorDataDto;
import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.repository.DeviceRepository;
import jakarta.annotation.PostConstruct;
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
    private final IMqttClient mqttClient;
    private final DeviceStatusService deviceStatusService;
    private final MqttConnectOptions options;

    public MqttService(
            IMqttClient mqttClient,
            DeviceRepository deviceRepository,
            SimpMessagingTemplate messagingTemplate,
            DeviceStatusService deviceStatusService,
            MqttConnectOptions options
    ) throws MqttException {
        this.mqttClient = mqttClient;
        this.deviceRepository = deviceRepository;
        this.messagingTemplate = messagingTemplate;
        this.deviceStatusService = deviceStatusService;
        this.options = options;
    }

    @PostConstruct
    public void init() {
        setupCallback();
        connectAndSubscribe();
    }

    private void setupCallback() {
        mqttClient.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connection to MQTT broker lost!");
                connectAndSubscribe();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String payload = new String(message.getPayload());

                if (topic.endsWith("/telemetry")) {
                    handleTelemetry(topic, payload);
                } else if (topic.endsWith("/status")) {
                    deviceStatusService.handleStatusUpdate(topic, payload);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });
    }

    public void connectAndSubscribe() {
        new Thread(() -> {
            while (true) {
                try {
                    if (!mqttClient.isConnected()) {
                        mqttClient.connect(options);
                    }

                    mqttClient.subscribe("soil/device/+/telemetry");
                    mqttClient.subscribe("soil/device/+/status");

                    break;
                } catch (Exception e) {
                    System.out.println("MQTT not ready. Retrying in 5s...");
                   try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ignored) {}
                }
            }
        }).start();
    }

    private void handleTelemetry(String topic, String payload) {
        try {
            SensorDataDto data = objectMapper.readValue(payload, SensorDataDto.class);

            System.out.println("Humidity: " + data.humidity());
            System.out.println("Temp: " + data.temperature());

            handleSensorData(topic, data);

        } catch (Exception e) {
            System.err.println("Invalid MQTT payload: " + e.getMessage());
        }
    }

    private void handleSensorData(String topic, SensorDataDto payload) {
        String[] parts = topic.split("/");

        if (parts.length > 2) {
            String deviceKey = parts[2];

            Optional<Device> optionalDevice = deviceRepository.findByApiKey(deviceKey);

            optionalDevice.ifPresent(device -> {
                device.setLastHumidity(payload.humidity());
                device.setLastTemperature(payload.temperature());
                device.setLastSeen(Instant.now());

                deviceRepository.save(device);

                messagingTemplate.convertAndSend("/topic/device/" + device.getId().toString(), payload);
            });
        }
    }
}
