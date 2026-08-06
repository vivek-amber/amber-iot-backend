package com.device.dto;

import lombok.Data;

@Data
public class CommandResponse {

    private Long cmi;

    private Long vrs;

    private String sts;

    private String cmd;

    private Object val;
}