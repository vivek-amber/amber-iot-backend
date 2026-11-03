package com.device.dto;

import com.device.enums.DeviceType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder  
public class DeviceResponse {
    
    private String companyId;
    private String deviceId;
    private String deviceName;
    private String modelId;
    private DeviceType deviceType;
}
