package com.device.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommandDto {

    @NotBlank(message="Company Id is required to send command")
    private String companyId;

    @NotBlank(message="Device Id is required to send command")
    private String deviceId;

    @NotBlank(message="Command is required")
    private String cmd;

    @NotNull(message="Value is required to send Command")
    private Integer val;
}