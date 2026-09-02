package com.automation.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.automation.dto.CommandDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    
    private static final String TOPIC = "my_topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public void sendMessage(CommandDto message) {
        kafkaTemplate.send(TOPIC, message.getDeviceId(), message);
    }
}
