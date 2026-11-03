package com.device.dto;

import com.device.enums.ErrorLevel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public class DeviceErrorDefinition {

    @NotNull 
    private Integer value;

    @NotBlank 
    private String code;

    @NotBlank
    private String title;
    
    @NotNull
    private ErrorLevel level;
}