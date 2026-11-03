package com.device.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import com.device.router.MqttMessageRouter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MqttCallbackHandler implements MqttCallback {

    private final MqttMessageRouter router;


    @Override
    public void connectionLost(Throwable cause) {
       
        log.error("Mqtt Connection lost : ", cause);

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        router.route(topic, message.getPayload());

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        
        log.info("Delivery Complete for token : "+ token);
    }

}
