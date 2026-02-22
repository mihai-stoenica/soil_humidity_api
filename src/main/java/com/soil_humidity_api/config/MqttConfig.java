package com.soil_humidity_api.config;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    private static final String MQTT_PUBLISHER_ID = "spring-boot-api";
    private static final String MQTT_SERVER_ADDRESS = "tcp://mqtt:1883";

    @Bean
    public IMqttClient mqttClient() throws Exception {

        IMqttClient client = new MqttClient(MQTT_SERVER_ADDRESS, MQTT_PUBLISHER_ID);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);

        client.connect(options);

        return client;
    }
}
