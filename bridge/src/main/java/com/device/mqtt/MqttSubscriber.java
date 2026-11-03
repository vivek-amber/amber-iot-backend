package com.device.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.stereotype.Component;

import com.device.exception.MqttSubscribeException;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MqttSubscriber {

    private final MqttClient mqttClient;

    @PostConstruct
    public void subscribe() {
         
        try {

            if (!mqttClient.isConnected()) {
                log.error("Mqtt Client Disconnected");
                throw new MqttSubscribeException("Mqtt Client Disconnected");
            }

            mqttClient.subscribe("company/+/device/+/#",1);
            log.info("Successfully subscribed to company/+/device/+/#");
        } catch (Exception e) {

            log.error("Mqtt Subscription Failed", e);
        }
    }
}
