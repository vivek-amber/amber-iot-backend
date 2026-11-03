package com.device.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.device.dto.ControlState;
import com.device.dto.DeviceShadow;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShadowService {
    
    private final RedisTemplate<String, String> redisTemplate;

    private final RedisService redisService;

    public Long nextVersion(String deviceId, String control) {

        return redisTemplate.opsForValue()
                .increment("version:" + deviceId + ":" + control);
    }
    
    public void updateDesiredState(
        String deviceId,
        String companyId,
        String control,
        Object value,
        Long version                
    ) {

        DeviceShadow shadow = getOrCreate(deviceId, companyId);

        shadow.getDesiredState().put(
                control,
                ControlState.builder()
                        .value(value)
                        .version(version)
                        .build());

        save(shadow);

    }
    
    public void updateReportedState(
        String deviceId,
        String companyId,
        String control,
        Object value,
        Long version                                
    ) {

        DeviceShadow shadow = getOrCreate(deviceId, companyId);

        ControlState current = shadow.getReportedState().get(control);

        if (current != null && version <= current.getVersion()) {

            return;
        }

        shadow.getReportedState().put(
                control,
                ControlState.builder()
                        .value(value)
                        .version(version)
                        .build());

        save(shadow);
    }

    public void syncFromDeviceState(
            String deviceId,
            String companyId,
            Map<String, ControlState> state) {

        DeviceShadow shadow = getOrCreate(deviceId, companyId);

        Map<String, ControlState> desired = shadow.getDesiredState() != null
                ? new HashMap<>(shadow.getDesiredState())
                : new HashMap<>();

        Map<String, ControlState> reported = shadow.getReportedState() != null
                ? new HashMap<>(shadow.getReportedState())
                : new HashMap<>();

        desired.putAll(state);
        reported.putAll(state);

        shadow.setDesiredState(desired);
        shadow.setReportedState(reported);

        save(shadow);
    }
    
    private DeviceShadow getOrCreate(String deviceId, String companyId) {

        String key = buildKey(companyId, deviceId);

        DeviceShadow shadow = redisService.get(key, DeviceShadow.class);

        if (shadow != null) {
            return shadow;
        }

        shadow = DeviceShadow.builder()
                .deviceId(deviceId)
                .companyId(companyId)
                .desiredState(new HashMap<>())
                .reportedState(new HashMap<>())
                .build();

        save(shadow);
        return shadow;
    }
    
    public DeviceShadow getShadow(String deviceId, String companyId) {
        
        String key = buildKey(companyId, deviceId);

        return redisService.get(key, DeviceShadow.class);
    }
    
    private void save(DeviceShadow shadow) {

        String key = buildKey(shadow.getCompanyId(), shadow.getDeviceId());

        redisService.save(key, shadow);
    }
    
    private String buildKey(String companyId, String deviceId) {

        return String.format(
                "shadow:%s:%s",
                companyId,
                deviceId);
    }

}
