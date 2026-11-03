package com.device.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.device.dto.CreateDeviceErrorRequest;
import com.device.dto.DeviceErrorDefinition;
import com.device.dto.UpdateDeviceErrorRequest;
import com.device.entity.DeviceErrorEntity;
import com.device.helper.ApiResponse;
import com.device.repository.DeviceErrorRepository;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceErrorServiceImpl implements DeviceErrorService {

    private final DeviceErrorRepository deviceErrorRepository;

    @Override
    @Transactional 
    public ApiResponse<Map<Object, Object>> create(CreateDeviceErrorRequest request) {

        Set<Integer> values = new HashSet<>();
        Set<String> codes = new HashSet<>();

        for (DeviceErrorDefinition error : request.getErrors()) {

            if (!values.add(error.getValue())) {
            throw new BadRequestException(
                "Duplicate error value: " + error.getValue()
            );
            }

            if (!codes.add(error.getCode().trim().toUpperCase())) {
            throw new BadRequestException(
                "Duplicate error code: " + error.getCode()
            );
            }
        }

        DeviceErrorEntity entity = new DeviceErrorEntity();
        entity.setId(request.getId());
        entity.setErrors(request.getErrors());

        deviceErrorRepository.save(entity);
        return ApiResponse.success("Error catalog saved successfully", Collections.emptyMap());
    }

    @Override
    public ApiResponse<DeviceErrorEntity> get(String id) {

        DeviceErrorEntity entity = deviceErrorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error catalog not found"));

        return ApiResponse.success(
                "Error catalog fetched successfully",
                entity);
    }

    @Override
    public ApiResponse<Map<Object, Object>> update(
            String id,
            UpdateDeviceErrorRequest request) {

        DeviceErrorEntity entity = deviceErrorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error catalog not found"));

        Map<Object, Object> updatedFields = new HashMap<>();

        if (request.getErrors() != null) {
            entity.setErrors(request.getErrors());
            updatedFields.put("errors", request.getErrors());
        }

        deviceErrorRepository.save(entity);

        return ApiResponse.success("Error catalog updated successfully",Collections.emptyMap());
    }

    @Override
    public ApiResponse<Map<Object, Object>> delete(String id) {

        DeviceErrorEntity entity = deviceErrorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error catalog not found"));

        deviceErrorRepository.delete(entity);

        return ApiResponse.success("Error catalog deleted successfully",Collections.emptyMap());
    }
}