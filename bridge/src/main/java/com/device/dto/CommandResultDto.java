package com.device.dto;

import com.device.enums.CommandStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class CommandResultDto {

    private String deviceId;

    private String cmd;

    private Integer value;

    private CommandStatus status;

    private String reason;
}