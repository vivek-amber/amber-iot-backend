package com.device.service;

import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.device.helpers.TopicFormatter;

import com.device.dto.PendingCommand;
import com.device.helpers.DataFormatter;
import com.device.mqtt.MqttPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class CommandRetryScheduler {

    private static final long RETRY_INTERVAL_MS = 5_000;

    private final RedisService redisService;
    private final MqttPublisher mqttPublisher;

    @Scheduled(fixedDelay = RETRY_INTERVAL_MS)
    public void retryPendingCommands() {

        Set<String> pendingKeys = redisService.getPendingKeys();

        for (String key : pendingKeys) {

            PendingCommand command = redisService.getPendingCommandByKey(key);

            // Coomand not found
            if (command == null) {

                continue;
            }
            
            Long sentAt = command.getSentAt();

            // Retry time not reached
            if (System.currentTimeMillis() < sentAt + RETRY_INTERVAL_MS) {
                continue;
            }
            
            // Max retry reached
            if (command.getRetryCount() >= command.getMaxRetry()) {

                redisService.deletePendingCommand(command.getDeviceId(),command.getCmd()              );
                continue;
            }

            String topic = TopicFormatter.commandTopic(command.getCompanyId(), command.getDeviceId());
            String data = DataFormatter.commandValue(command.getCmi(), command.getVrs(), command.getCmd(), command.getVal());

            mqttPublisher.publish(topic, data);
            
            command.setRetryCount(command.getRetryCount() + 1);
        
            command.setSentAt(System.currentTimeMillis());
        
            redisService.updatePendingCommand(command.getDeviceId(),command.getCmd(),command);

        }
    }
}