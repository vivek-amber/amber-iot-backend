package com.device.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.device.dto.DeviceErrorDefinition;
import com.device.dto.DeviceErrorEvent;
import com.device.entity.DeviceRuntimeErrorEntity;
import com.device.entity.ModelEntity;
import com.device.exception.ModelNotFoundException;
import com.device.helper.ApiResponse;
import com.device.repository.DeviceRuntimeErrorRepository;
import com.device.repository.ModelRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class RuntimeErrorService {

    private final DeviceRuntimeErrorRepository repository;
    private final ModelRepository modelRepository;

    @Transactional 
    public void process(DeviceErrorEvent event) {

        DeviceRuntimeErrorEntity entity = repository.findByDeviceIdAndCompanyId(
                event.getDeviceId(),
                event.getCompanyId())
                .orElseGet(() -> {
                    DeviceRuntimeErrorEntity e = new DeviceRuntimeErrorEntity();

                    e.setDeviceId(event.getDeviceId());
                    e.setCompanyId(event.getCompanyId());
                    e.setActiveErrors(new ArrayList<>());

                    return e;
                });

        List<Integer> errors = entity.getActiveErrors();

        if (!errors.contains(
                event.getValue())) {
            errors.add(event.getValue());
        }

        repository.save(entity);
    }

    public ApiResponse<List<Integer>> getErrors(String companyId, String deviceId) {

        DeviceRuntimeErrorEntity entity = repository.findByDeviceIdAndCompanyId(
                deviceId,
                companyId).orElse(null);

        return ApiResponse.success("Errors fetched sucessfully.",
                entity == null ? List.of() : entity.getActiveErrors());
    }

    public ApiResponse<List<DeviceErrorDefinition>> getByModelId(String modelId) {
        
        ModelEntity model = modelRepository.findById(modelId).orElseThrow(
            ()->new ModelNotFoundException("Requested model not exists")
        );

        return ApiResponse.success("Error catalog fetched successfully.", model.getErrorCatalog().getErrors());
    }
}
