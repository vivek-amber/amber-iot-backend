/*
 * @@file: 
 * @@brief: 
 * @@author: Vivek
 * @@date: 
 */
package com.device.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.device.dto.DeviceResponse;
import com.device.dto.RegisterDeviceRequest;
import com.device.dto.RenameDeviceRequest;
import com.device.entity.DeviceEntity;
import com.device.helper.ApiResponse;
import com.device.service.DeviceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<ApiResponse<DeviceEntity>> register(@Valid @RequestBody RegisterDeviceRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(deviceService.registerDevice(request));
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<ApiResponse<DeviceEntity>> getDevice(@PathVariable String deviceId) {

        return ResponseEntity.ok(deviceService.getDevice(deviceId));
    }

    @PatchMapping("/{deviceId}/name")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> rename(
        @PathVariable String deviceId,
        @Valid @RequestBody RenameDeviceRequest request
    ) {
        return ResponseEntity.ok(deviceService.renameDevice(deviceId, request)
    );
}

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<DeviceResponse>>> getUserDevices() {

        return ResponseEntity.ok(deviceService.getUserDevices());
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<ApiResponse<Object>> remove(@PathVariable String deviceId) {

        return ResponseEntity.ok(deviceService.removeDevice(deviceId));
    }
}