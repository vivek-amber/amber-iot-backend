package com.device.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.device.dto.ControlState;
import com.device.dto.DeviceShadow;
import com.device.entity.DeviceStateEntity;
import com.device.repository.DeviceStateRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceStateService {

    private final DeviceStateRepository repository;
    private final ObjectMapper objectMapper;
    private final ShadowService shadowService;

    public void save(DeviceShadow shadow) {

            try {

                    DeviceStateEntity entity = getOrCreate(shadow.getCompanyId(), shadow.getDeviceId());
                
                ObjectNode node = objectMapper.createObjectNode();

                shadow.getReportedState().forEach(
                        (k,v)-> node.set(k, objectMapper.valueToTree(v.getValue()))
                );
                entity.setState(node);
                repository.save(entity);

            } catch (Exception e) {
                throw new RuntimeException("Failed to save device state",e);
            }
    }

    public void processStatus(String companyId, String deviceId, String payload) {

            // 1. Parse status payload
            JsonNode status = null;

            try {
                    status = objectMapper.readTree(payload);

            } catch (Exception e) {
                    throw new RuntimeException("Status Payload can't be read");
            }

            // 2. Process and store in redis
            Map<String, ControlState> state = new HashMap<>();

            for (Map.Entry<String, JsonNode> entry : status.properties()) {

                    Object value = objectMapper.convertValue(entry.getValue(), Object.class);

                    state.put(
                                    entry.getKey(),
                                    ControlState.builder()
                                                    .value(value)
                                                    .version(1L)
                                                    .build());

            }
            shadowService.syncFromDeviceState(deviceId, companyId, state);

            // 3. Persist in db
            DeviceStateEntity entity = getOrCreate(companyId, deviceId);
            entity.setState(status);
            repository.save(entity);
    }
    
    private DeviceStateEntity getOrCreate(String companyId, String deviceId) {
        
        return repository.findByCompanyIdAndDeviceId(companyId, deviceId)
        .orElse(
                DeviceStateEntity.builder()
                .companyId(companyId)
                .deviceId(deviceId)
                .build()
        );
    }
}