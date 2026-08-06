package com.device.service;

import java.util.List;

import com.device.dto.RegisterDeviceRequest;
import com.device.entity.DeviceEntity;
import com.device.helper.ApiResponse;

public interface DeviceService {

    ApiResponse<DeviceEntity> registerDevice(RegisterDeviceRequest request);

    ApiResponse<DeviceEntity> getDevice(String deviceId);

    ApiResponse<List<DeviceEntity>> getUserDevices(String userId);

    ApiResponse<Object> removeDevice(String deviceId);
}