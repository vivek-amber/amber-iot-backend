package com.device.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceShadow {

    private String deviceId;

    private String companyId;

    private Map<String, ControlState> desiredState;

    private Map<String, ControlState> reportedState;
}