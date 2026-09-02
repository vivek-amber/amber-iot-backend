package com.device.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.device.dto.CommandDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final DeviceService deviceService;
    
    @KafkaListener(topics = "my_topic", groupId = "group_id_v1")
    public void consume(CommandDto request) {

        deviceService.sendCommand(request);
    }
}
