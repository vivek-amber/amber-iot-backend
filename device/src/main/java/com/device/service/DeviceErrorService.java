package com.device.service;

import java.util.Map;

import com.device.dto.CreateDeviceErrorRequest;
import com.device.dto.UpdateDeviceErrorRequest;
import com.device.entity.DeviceErrorEntity;
import com.device.helper.ApiResponse;

public interface DeviceErrorService {

    ApiResponse<Map<Object, Object>> create(CreateDeviceErrorRequest request);

    ApiResponse<DeviceErrorEntity> get(String id);

    ApiResponse<Map<Object, Object>> update(String id,UpdateDeviceErrorRequest request);

    ApiResponse<Map<Object, Object>> delete(String id);
}