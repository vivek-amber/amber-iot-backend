package com.device.service;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.device.dto.CommandResponse;
import com.device.dto.PendingCommand;
import com.device.helpers.TopicFormatter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private static final Duration TTL = Duration.ofMinutes(5);

    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;

    public void savePendingCommand(String deviceId,PendingCommand command) {

        try {

            String key = buildKey(deviceId,command.getCmi());

            String value = objectMapper.writeValueAsString(command);

            redisTemplate.opsForValue().set(key, value, TTL);

            redisTemplate.opsForSet().add("pending:commands", key);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize pending command",e);
        }
    }

    public PendingCommand getPendingCommand(String deviceId,Long cmdId) {

        try {

            String key = buildKey(deviceId, cmdId);

            String value = redisTemplate.opsForValue().get(key);

            if (value == null) {
                return null;
            }

            return objectMapper.readValue(value,PendingCommand.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize pending command",e);
        }
    }

    public void deletePendingCommand(String deviceId,Long cmdId) {

        String key = buildKey(deviceId, cmdId);

        redisTemplate.delete(key);
        redisTemplate.opsForSet().remove("pending:commands", key);
    }

    private String buildKey(String deviceId, Long cmdId) {

        return String.format("pending:device:%s:cmd:%s", deviceId, cmdId);
    }
    
    public Set<String> getPendingKeys() {

        Set<String> keys = redisTemplate.opsForSet().members("pending:commands");

        return keys == null ? Collections.emptySet() : keys;
    }
    
    public void updatePendingCommand(String deviceId, PendingCommand command) {

        savePendingCommand(deviceId, command);
    }
    
    public boolean exists(String deviceId, Long cmdId) {

        return Boolean.TRUE.equals(redisTemplate.hasKey(buildKey(deviceId, cmdId)));
    }

    public PendingCommand getPendingCommandByKey(String key) {

        try {

            String value =redisTemplate.opsForValue().get(key);

            if (value == null) {
                return null;
            }

        return objectMapper.readValue(value,PendingCommand.class);

    } catch (Exception e) {

        throw new RuntimeException("Failed to deserialize pending command",e);
    }
}
    
    public void processAck(String topic, String payload) {

        try {
    
            CommandResponse response =objectMapper.readValue(payload,CommandResponse.class);
    
            String deviceId =TopicFormatter.extractDeviceId(topic);
    
            PendingCommand pending =getPendingCommand(deviceId,response.getCmi());
    
            if (pending == null) {
    
                log.warn("No pending command found for cmdId={}",response.getCmi());
    
                return;
            }
    
            if ("SUCCESS".equals(response.getSts())) {
    
                deletePendingCommand(deviceId,response.getCmi());
    
                log.info("Command completed successfully. deviceId={}, cmdId={}",deviceId,response.getCmi());
            }
    
        } catch (JsonProcessingException e) {
    
            log.error( "Failed to parse ACK payload. Topic={}, Payload={}",topic,payload,e);
        }
    }
}
