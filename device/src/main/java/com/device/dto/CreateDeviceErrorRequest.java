package com.device.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data 
public class CreateDeviceErrorRequest {

    @NotBlank(message = "Catalog id is required")
    private String id;

    @NotEmpty (message = "At least one error definition is required")
    private List<@Valid DeviceErrorDefinition> errors;
}