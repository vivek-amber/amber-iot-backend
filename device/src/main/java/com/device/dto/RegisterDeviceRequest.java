package com.device.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDeviceRequest {

    @NotBlank(message = "User id is required")
    private String userId;

    @NotBlank(message = "Device id is required")
    private String deviceId;

    @NotBlank(message = "Device name is required")
    private String deviceName;

    @NotBlank(message = "Model id is required")
    private String modelId;

    //@NotBlank(message = "Firmware Version id is required")
    //private String firmwareVersion;
}