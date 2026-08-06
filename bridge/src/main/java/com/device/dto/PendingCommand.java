package com.device.dto;

import lombok.Data;

@Data
public class PendingCommand {

    private Long cmi;

    private Long vrs;

    private String cmd;

    private Object val;

    private Long sentAt;

    private Integer retryCount;

    private Integer maxRetry;

    private String deviceId;
    
    private String companyId;
}