package com.automation.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class CreateScheduleRequest {

    @NotBlank(message = "Scene name is required")
    @Size(max = 100, message = "Scene name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Enabled status is required")
    private Boolean enabled;

    @Valid
    @NotNull(message = "At least one device is required")
    private DeviceAction device;

    @NotNull(message = "Schedule Trigger is required")
    private ScheduleTrigger scheduleTrigger;
}