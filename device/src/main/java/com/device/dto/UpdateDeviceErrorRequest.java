package com.device.dto;

import java.util.List;

import lombok.Data;

@Data 
public class UpdateDeviceErrorRequest {

    private List<DeviceErrorDefinition> errors;
}
