package com.device.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class RenameDeviceRequest {

    @NotBlank (message = "Device name is required")
    @Size (min = 1, max = 50)
    private String deviceName;
}