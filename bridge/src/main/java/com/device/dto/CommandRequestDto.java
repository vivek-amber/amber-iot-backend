package com.device.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommandRequestDto {
    
    private Long cmi;

    private Long vrs;

    private String cmd;

    private Object val;
}
