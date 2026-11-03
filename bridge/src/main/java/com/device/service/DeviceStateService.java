package com.device.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.device.dto.ControlState;
import com.device.dto.DeviceShadow;
import com.device.dto.DeviceStateResponse;
import com.device.dto.StatusEvent;
import com.device.entity.DeviceStateEntity;
import com.device.helpers.ApiResponse;
import com.device.proto.DeviceState;
import com.device.proto.DeviceStatus;
import com.device.repository.DeviceStateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceStateService {

    private final DeviceStateRepository repository;
    private final ObjectMapper objectMapper;
    private final ShadowService shadowService;
    private final RedisTemplate<String, String> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public void save(DeviceShadow shadow) {

            try {

                    DeviceStateEntity entity = getOrCreate(shadow.getCompanyId(), shadow.getDeviceId());

                    ObjectNode node = objectMapper.createObjectNode();

                    shadow.getReportedState().forEach(
                                    (k, v) -> node.set(k, objectMapper.valueToTree(v.getValue())));
                    entity.setState(node);
                    repository.save(entity);

            } catch (Exception e) {
                    throw new RuntimeException("Failed to save device state", e);
            }
    }

    @Transactional 
    public void processState(
                    String companyId,
                    String deviceId,
                    DeviceState payload) {
            if (payload == null) {
                    log.warn(
                                    "Null state payload received: companyId={}, deviceId={}",
                                    companyId,
                                    deviceId);
                    return;
            }

            String command = payload.getCmd();

            if (command == null || command.isBlank()) {
                    log.warn(
                                    "Empty state command received: companyId={}, deviceId={}",
                                    companyId,
                                    deviceId);
                    return;
            }

            int value = payload.getVal();

            Map<String, ControlState> state = new HashMap<>();

            state.put(command,ControlState.builder()
                                            .value(value)
                                            .version(1L)
                                            .build());

            shadowService.syncFromDeviceState(
                            deviceId,
                            companyId,
                            state);

            try {
                    DeviceStateEntity entity = getOrCreate(
                                    companyId,
                                    deviceId);

                    /*
                     * Preserve existing properties because state updates
                     * arrive one by one from the device.
                     */
                    ObjectNode node;

                    if (entity.getState() != null &&
                                    entity.getState().isObject()) {
                            node = ((ObjectNode) entity.getState())
                                            .deepCopy();
                    } else {
                            node = objectMapper.createObjectNode();
                    }

                    /*
                     * Update only the received command.
                     */
                    node.put(command,value);

                    entity.setState(node);
                    repository.save(entity);

                    messagingTemplate.convertAndSend(
                        "/topic/company/" + companyId + "/device/" + deviceId+ "/state",
                                    (Object) Map.of(command, value));

            } catch (Exception e) {
                    log.error(
                                    "Error while processing device state: companyId={}, deviceId={}, cmd={}",
                                    companyId,
                                    deviceId,
                                    command,
                                    e);
            }
    }
    
    public void processStatus(String companyId, String deviceId, DeviceStatus payload) {

        if (!payload.getOnline()) {
                redisTemplate.delete(buildKey(companyId, deviceId));
        } else {
                redisTemplate.opsForValue().set(
                        buildKey(companyId, deviceId),
                         "true",
                        Duration.ofSeconds(10));
        }

        messagingTemplate.convertAndSend("/topic/company/"+ companyId+ "/device/" + deviceId + "/status", new StatusEvent(payload.getOnline(),deviceId));
    }

    private String buildKey(String companyId, String deviceId) {

            return String.format("presence:%s:%s", companyId, deviceId);
    }

    private DeviceStateEntity getOrCreate(String companyId, String deviceId) {

            return repository.findByCompanyIdAndDeviceId(companyId, deviceId)
                            .orElse(
                                            DeviceStateEntity.builder()
                                                            .companyId(companyId)
                                                            .deviceId(deviceId)
                                                            .build());
    }

    public ApiResponse<DeviceStateResponse> getState(String companyId,String deviceId) {

            DeviceStateEntity state = repository
                            .findByCompanyIdAndDeviceId(companyId, deviceId)
                            .orElseThrow(() -> new RuntimeException("Device not found"));

            DeviceStateResponse response = DeviceStateResponse.builder()
                            .deviceId(state.getDeviceId())
                            .companyId(state.getCompanyId())
                            .state(objectMapper.convertValue(
                                            state.getState(),
                                            Object.class))
                            .updatedAt(state.getUpdatedAt())
                            .build();

            return ApiResponse.success(
                            "Device state fetched",
                            response);
    }
}