package com.automation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Action {

    @NotBlank(message = "Command is required")
    @Size(max = 10, message = "Command cannot exceed 10 characters")
    private String cmd;

    @NotBlank(message = "Command value is required")
    @Size(max = 10, message = "Command value cannot exceed 10 characters")
    private String val;
}