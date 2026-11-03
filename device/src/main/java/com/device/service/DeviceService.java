package com.device.service;

import java.util.List;
import java.util.Map;

import com.device.dto.DeviceResponse;
import com.device.dto.RegisterDeviceRequest;
import com.device.dto.RenameDeviceRequest;
import com.device.entity.DeviceEntity;
import com.device.helper.ApiResponse;

public interface DeviceService {

    ApiResponse<DeviceEntity> registerDevice(RegisterDeviceRequest request);

    ApiResponse<DeviceEntity> getDevice(String deviceId);

    ApiResponse<List<DeviceResponse>> getUserDevices();

    ApiResponse<Object> removeDevice(String deviceId);

    ApiResponse<Map<Object, Object>> renameDevice(String deviceId,RenameDeviceRequest request);
}