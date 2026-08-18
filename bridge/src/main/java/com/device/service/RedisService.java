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

    public void savePendingCommand(String deviceId, String command, PendingCommand pending) {

        try {

            String key = buildKey(deviceId, command);

            String value = objectMapper.writeValueAsString(pending);

            redisTemplate.opsForValue().set(key, value, TTL);

            redisTemplate.opsForSet().add("pending:commands", key);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize pending command",e);
        }
    }

    public PendingCommand getPendingCommand(String deviceId, String command) {
        try {

            String key = buildKey(deviceId, command);

            String value = redisTemplate.opsForValue().get(key);

            if (value == null) {
                return null;
            }

            return objectMapper.readValue(
                    value,
                    PendingCommand.class);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to deserialize pending command",
                    e);
        }
    }

    public void deletePendingCommand(String deviceId, String command) {

        String key = buildKey(deviceId, command);

        redisTemplate.delete(key);

        redisTemplate.opsForSet().remove("pending:commands", key);
    }

    private String buildKey(String deviceId, String command) {

        return String.format(
                "pending:device:%s:control:%s",
                deviceId,
                command);
    }
    
    public Set<String> getPendingKeys() {

        Set<String> keys = redisTemplate.opsForSet().members("pending:commands");

        return keys == null ? Collections.emptySet() : keys;
    }
    
    public void updatePendingCommand(String deviceId, String command, PendingCommand pending) {

        savePendingCommand(deviceId, command, pending);
    }
    
    public boolean exists(String deviceId, String command) {
        
        return Boolean.TRUE.equals(redisTemplate.hasKey(buildKey(deviceId, command)));
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
    
            PendingCommand pending = getPendingCommand(
                    deviceId,
                    response.getCmd());

            if (pending == null) {
                return;
            }

            if (!pending.getCmi().equals(response.getCmi())) {
                return;
            }

            if ("SUCCESS".equals(response.getSts())) {

                deletePendingCommand(deviceId,response.getCmd());
            }
    
        } catch (JsonProcessingException e) {
    
            log.error( "Failed to parse ACK payload. Topic={}, Payload={}",topic,payload,e);
        }
    }
}
