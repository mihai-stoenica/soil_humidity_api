package com.soil_humidity_api.config;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    @Bean
    public MqttConnectOptions mqttConnectOptions(
            @Value("${MQTT_USER}") String user,
            @Value("${MQTT_PWD}") String password
    ) {

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        options.setUserName(user);
        options.setPassword(password.toCharArray());

        return options;
    }

    @Bean
    public IMqttClient mqttClient() throws Exception {
        return new MqttClient("tcp://emqx:1883", "spring-boot-api");
    }
}
