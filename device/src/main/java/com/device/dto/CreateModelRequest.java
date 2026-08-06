package com.device.dto;

import com.device.enums.DeviceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateModelRequest {

    @NotBlank(message = "Model id is required")
    private String id;

    @NotBlank(message = "Model name is required")
    private String modelName;

    @NotNull(message = "Device type is required")
    private DeviceType deviceType;

    private Boolean active = true;
}
