package com.device.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.device.dto.DeviceResponse;
import com.device.dto.RegisterDeviceRequest;
import com.device.dto.RenameDeviceRequest;
import com.device.entity.DeviceEntity;
import com.device.exception.DeviceNotFoundException;
import com.device.helper.ApiResponse;
import com.device.mapper.DeviceMapper;
import com.device.repository.DeviceRepository;
import com.sucurity.context.SecurityContextUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    @Override
    public ApiResponse<DeviceEntity> registerDevice(RegisterDeviceRequest request) {

        DeviceEntity device = new DeviceEntity();

        String userId = SecurityContextUtil.currentUserId();

        if (userId == null || userId.isBlank()) {
            throw new RuntimeException("Unauthorize");
        }

        Optional<DeviceEntity> existing = deviceRepository.findByDeviceIdAndUserId(request.getDeviceId(),userId);

        if(existing.isPresent()){
            return ApiResponse.success("Device already registered",existing.get());
        }

        device.setUserId(userId);
        device.setDeviceId(request.getDeviceId());
        device.setDeviceName(request.getDeviceName());
        device.setModelId(request.getModelId());
        device.setCompanyId(request.getCompanyId());
        device.setDeviceType(request.getDeviceType());

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
    public ApiResponse<List<DeviceResponse>> getUserDevices() {

        String userId = SecurityContextUtil.currentUserId();
        List<DeviceEntity> devices = deviceRepository.findByUserId(userId);

        List<DeviceResponse> data = devices.stream()
        .map((d)->DeviceMapper.toDto(d))
                .toList();
        
        return ApiResponse.success("Devices fetched successfully", data);
    }

    @Override
    public ApiResponse<Object> removeDevice(String deviceId) {

        String userId = SecurityContextUtil.currentUserId();

        if (userId == null || userId.isBlank()) {

            throw new RuntimeException("Unauthorized.");
        }

        DeviceEntity device = deviceRepository.findByDeviceIdAndUserId(deviceId, userId)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found"));

        deviceRepository.delete(device);
        return ApiResponse.success("Device removed successfully", null);
    }
    
    @Override
    public ApiResponse<Map<Object,Object>> renameDevice(String deviceId,RenameDeviceRequest request) {

        String userId = SecurityContextUtil.currentUserId();

        if (userId == null || userId.isBlank()) {
            throw new RuntimeException("Unauthorized");
        }

        DeviceEntity device =
            deviceRepository.findByDeviceIdAndUserId(deviceId, userId)
                    .orElseThrow(() ->
                            new DeviceNotFoundException("Device not found"));

        device.setDeviceName(request.getDeviceName().trim());

        deviceRepository.save(device);

        return ApiResponse.success(
            "Device renamed successfully",
            Collections.emptyMap()
        );
    }
}