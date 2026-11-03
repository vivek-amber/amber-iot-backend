package com.device.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.device.dto.DeviceErrorEvent;
import com.device.proto.DeviceError;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class DeviceErrorService {

    private final SimpMessagingTemplate messagingTemplate;
    private final KafkaTemplate<String, DeviceErrorEvent> kafkaTemplate;

    public void processError(String companyId,String deviceId,DeviceError error) {

        DeviceErrorEvent event = DeviceErrorEvent.builder()
                .companyId(companyId)
                .deviceId(deviceId)
                .value(error.getValue())        
                .build();

        // websocket
        messagingTemplate.convertAndSend(
                "/topic/company/" +
                        companyId +
                        "/device/" +
                        deviceId +
                        "/error",
                event);

        // persistence
        kafkaTemplate.send(
                "device-error",
                deviceId,
                event);
    }
}
