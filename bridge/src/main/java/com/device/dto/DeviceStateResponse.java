package com.device.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
public class DeviceStateResponse {
    private String deviceId;
    private String companyId;
    private Object state;
    private Instant updatedAt;
}