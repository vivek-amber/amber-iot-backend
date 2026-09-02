package com.automation.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateSceneRequest {

    @NotBlank(message = "Scene name is required")
    @Size(max = 100, message = "Scene name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Enabled status is required")
    private Boolean enabled;

    @Valid
    @NotEmpty(message = "At least one device is required")
    @Size(max = 100, message = "A scene cannot contain more than 100 devices")
    private List<@NotNull(message = "Device action cannot be null") @Valid DeviceAction> 
    devices;
}