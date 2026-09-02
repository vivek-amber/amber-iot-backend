package com.automation.dto;

import java.time.Instant;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SceneResponse {

    private String id;

    private String name;

    private Boolean enabled;

    private List<DeviceAction> devices;

    private Instant createdAt;

    private Instant updatedAt;
}