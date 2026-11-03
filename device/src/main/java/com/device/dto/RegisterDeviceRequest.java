package com.device.dto;

import com.device.enums.DeviceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterDeviceRequest {

    @NotBlank(message = "Device id is required")
    private String deviceId;

    @NotBlank(message = "Device name is required")
    private String deviceName;

    @NotBlank(message = "Model id is required")
    private String modelId;

    @NotBlank(message = "Company ID is required")
    private String companyId;

    @NotNull
    private DeviceType deviceType;
    //@NotBlank(message = "Firmware Version id is required")
    //private String firmwareVersion;
}