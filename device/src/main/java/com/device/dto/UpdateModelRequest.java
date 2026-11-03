package com.device.dto;

import com.device.enums.DeviceType;

import lombok.Data;

@Data 
public class UpdateModelRequest {
    
    private String modelName;

    private DeviceType deviceType;

    private Boolean active;

    private String errorCatalogId;
}
