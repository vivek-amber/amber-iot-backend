package com.device.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaveModelConfigRequest {

    @NotBlank(message = "Model id is required")
    private String modelId;

    @NotNull(message = "Configuration is required")
    @Valid
    private DeviceConfiguration configuration;
}