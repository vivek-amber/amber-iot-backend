package com.device.dto;


import com.device.enums.Mode;

import lombok.Data;

@Data
public class ModeDefinition {

    private Mode code;

    private String displayName;

}