package com.device.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.device.dto.DeviceStateResponse;
import com.device.dto.DeviceStatusRequest;
import com.device.dto.StatusEvent;
import com.device.helpers.ApiResponse;
import com.device.service.DeviceStateService;
import com.device.service.PresenceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/bridge/device/status")
@RequiredArgsConstructor 
public class PresenceController {

    private final PresenceService presenceService;
    private final DeviceStateService deviceStateService;

    @PostMapping 
    public ResponseEntity<ApiResponse<StatusEvent>> getStatus(
            @Valid @RequestBody DeviceStatusRequest request) {

        return ResponseEntity.ok(presenceService.getStatus(request));
    }

    @GetMapping("/{companyId}/{deviceId}")
    public ResponseEntity<ApiResponse<DeviceStateResponse>> getState(
            @PathVariable String companyId,
            @PathVariable String deviceId) 
    {

        return ResponseEntity.ok(deviceStateService.getState(companyId,deviceId));
    }
}