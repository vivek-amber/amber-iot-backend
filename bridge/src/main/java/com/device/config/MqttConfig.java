package com.device.config;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.device.mqtt.MqttCallbackHandler;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MqttConfig {

    @Value("${mqtt.broker-url}")
    private String brokerUrl;

    @Value("${mqtt.client-id}")
    private String clientId;

    @Value("${mqtt.username}")
    private String userName;

    @Value("${mqtt.password}")
    private String password;

    private MqttClient mqttClient;

    private final MqttCallbackHandler callbackHandler;

    @Bean
    MqttClient mqttClient() throws Exception {

        try {

            MqttConnectOptions connectOptions = new MqttConnectOptions();

            connectOptions.setAutomaticReconnect(true);
            connectOptions.setCleanSession(true);
            connectOptions.setConnectionTimeout(60);
            connectOptions.setKeepAliveInterval(60);

            if (userName != null && !userName.isBlank()) {
                connectOptions.setUserName(userName);
            }

            if (password != null && !password.isBlank()) {
                connectOptions.setPassword(password.toCharArray());
            }

            String dynamicClientId = clientId + "-" + UUID.randomUUID();

            log.info("Broker: {}", brokerUrl);
            log.info("ClientId: {}", clientId);
            log.info("Username: {}", userName);
            mqttClient = new MqttClient(brokerUrl, dynamicClientId, new MemoryPersistence());
            mqttClient.setCallback(callbackHandler);
            mqttClient.connect(connectOptions);

            log.info("MQTT Connected Successfully");

            return mqttClient;

        } catch (Exception e) {
            log.error("MQTT unavailable", e);
        }

        return mqttClient;
    }

    @PreDestroy
    public void shutdownMqtt() {

        try {

            if (mqttClient != null) {

                if (mqttClient.isConnected()) {

                    log.info("Disconnecting MQTT...");
                    mqttClient.disconnect();
                    log.info("MQTT Disconnected");
                }

                mqttClient.close();
                log.info("MQTT Client Closed");
            }
        } catch (Exception e) {
            log.error("Error while closing MQTT", e);
            e.printStackTrace();
        }
    }
}
