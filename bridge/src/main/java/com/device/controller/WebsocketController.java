package com.device.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.device.dto.CommandDto;
import com.device.service.DeviceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WebsocketController {
    
    private final DeviceService deviceService;


    @MessageMapping("/cmd")
    public void sendCommand(@Valid CommandDto request) {
        deviceService.sendCommand(request);
    }
}
