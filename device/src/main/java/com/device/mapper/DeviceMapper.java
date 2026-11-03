package com.device.mapper;

import org.springframework.stereotype.Component;

import com.device.dto.DeviceResponse;
import com.device.entity.DeviceEntity;

@Component 
public class DeviceMapper {
    
    public static DeviceResponse toDto(DeviceEntity entity) {

        return  DeviceResponse.builder()
        .companyId(entity.getCompanyId())
        .deviceId(entity.getDeviceId())
        .deviceName(entity.getDeviceName())
        .deviceType(entity.getDeviceType())
        .modelId(entity.getModelId())
        .build();
    }
}
