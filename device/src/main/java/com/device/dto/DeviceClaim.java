package com.device.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder 
public class DeviceClaim {

    private String deviceId;
    private long timeStamp;
}
