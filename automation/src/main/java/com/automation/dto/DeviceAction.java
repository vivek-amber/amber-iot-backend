package com.automation.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeviceAction {

    @NotBlank(message = "Device ID is required")
    private String deviceId;

    @Valid
    @NotEmpty(message = "At least one action is required for each device")
    @Size(max = 50, message = "A device cannot contain more than 50 actions")
    private List<Action> actions;
}