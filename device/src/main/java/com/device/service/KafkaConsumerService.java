package com.device.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.device.dto.DeviceErrorEvent;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class KafkaConsumerService {

    private final RuntimeErrorService runtimeErrorService;

    @KafkaListener (topics = "device-error", groupId = "device-service")
    public void handle(DeviceErrorEvent event) {

        runtimeErrorService.process(event);
    }
}
