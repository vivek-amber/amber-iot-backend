package com.device.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.stereotype.Service;

import com.device.exception.MqttPublishException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttPublisher {

    private final MqttClient mqttClient;

    public void publish(String topic, String payload) {

        try {

            if (!mqttClient.isConnected()) {
                throw new MqttPublishException("MQTT Client is disconnected");
            }

            mqttClient.publish(topic, payload.getBytes(), 1, false);
            log.debug("Published topic : {} payload : {}", topic, payload);
            
            
        } catch (Exception e) {

            log.error("Failed to publish message to topic : {}", topic, e);
            throw new MqttPublishException("Failed to publish MQTT message", e);
        }
        
    }
}
