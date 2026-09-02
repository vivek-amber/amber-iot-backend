package com.automation.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScheduleResponse {

    private String id;

    private String name;

    private Boolean enabled;

    private DeviceAction device;

    private ScheduleTrigger scheduleTrigger;

    private Instant createdAt;

    private Instant updatedAt;
}