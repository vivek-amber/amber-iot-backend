package com.device.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.device.dto.RegisterDeviceRequest;
import com.device.entity.DeviceEntity;
import com.device.exception.DeviceNotFoundException;
import com.device.helper.ApiResponse;
import com.device.repository.DeviceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    @Override
    public ApiResponse<DeviceEntity> registerDevice(RegisterDeviceRequest request) {

        DeviceEntity device = new DeviceEntity();

        device.setUserId(request.getUserId());
        device.setDeviceId(request.getDeviceId());
        device.setDeviceName(request.getDeviceName());
        device.setModelId(request.getModelId());

        DeviceEntity savedDevice = deviceRepository.save(device);

        return ApiResponse.success("Device registered successfully",savedDevice);
    }

    @Override
    public ApiResponse<DeviceEntity> getDevice(String deviceId) {

        DeviceEntity device = deviceRepository.findById(deviceId)
                .orElseThrow(() ->
                        new DeviceNotFoundException("Device not found"));

        return ApiResponse.success("Device fetched successfully",device);
    }

    @Override
    public ApiResponse<List<DeviceEntity>> getUserDevices(String userId) {

        List<DeviceEntity> devices =deviceRepository.findByUserId(userId);

        return ApiResponse.success("Devices fetched successfully",devices);
    }

    @Override
    public ApiResponse<Object> removeDevice(String deviceId) {

        DeviceEntity device = deviceRepository.findById(deviceId)
                .orElseThrow(() ->
                        new DeviceNotFoundException("Device not found"));

        deviceRepository.delete(device);

        return ApiResponse.success("Device removed successfully", null);
        
    }
}