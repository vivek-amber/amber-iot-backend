package com.device.controller;

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

import com.device.dto.CreateDeviceErrorRequest;
import com.device.dto.UpdateDeviceErrorRequest;
import com.device.entity.DeviceErrorEntity;
import com.device.helper.ApiResponse;
import com.device.service.DeviceErrorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/device/error")
@RequiredArgsConstructor 
public class DeviceErrorController {

    private final DeviceErrorService deviceErrorService;

    @PostMapping 
    public ResponseEntity<ApiResponse<Map<Object, Object>>> create(@Valid @RequestBody CreateDeviceErrorRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(deviceErrorService.create(request));
    }

    @GetMapping ("/{id}")
    public ResponseEntity<ApiResponse<DeviceErrorEntity>> get(@PathVariable String id) {

        return ResponseEntity.ok(deviceErrorService.get(id));
    }

    @PatchMapping ("/{id}")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> update(@PathVariable String id,@RequestBody UpdateDeviceErrorRequest request) {

        return ResponseEntity.ok(deviceErrorService.update(id, request));
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> delete(@PathVariable String id) {

        return ResponseEntity.ok(deviceErrorService.delete(id));
    }
}
