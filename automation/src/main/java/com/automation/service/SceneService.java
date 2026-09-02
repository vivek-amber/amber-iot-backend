package com.automation.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.automation.dto.Action;
import com.automation.dto.CommandDto;
import com.automation.dto.CreateSceneRequest;
import com.automation.dto.DeviceAction;
import com.automation.dto.SceneResponse;
import com.automation.entity.SceneEntity;
import com.automation.exception.SceneAlreadyExistsException;
import com.automation.exception.SceneNotFoundException;
import com.automation.exception.UserNotValidException;
import com.automation.mapper.SceneMapper;
import com.automation.repository.SceneRepository;
import com.automation.utils.ApiResponse;
import com.sucurity.context.SecurityContextUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SceneService {

    private final SceneRepository sceneRepository;
    private final SceneMapper sceneMapper;
    private final KafkaProducerService kafkaProducerService;

    public ApiResponse<Map<Object, Object>> createScene(CreateSceneRequest request) {

        String userId = SecurityContextUtil.currentUserId();
        String companyId = SecurityContextUtil.currentCompanyId();

        if (userId == null || userId.isBlank() || companyId == null || companyId.isBlank()) {

            throw new UserNotValidException("Unauthorized user. Please Login again.");
        }

        Boolean existsByName = sceneRepository.existsByUserIdAndNameIgnoreCase(userId, request.getName());

        if (existsByName) {

            throw new SceneAlreadyExistsException("Scene with same name already exists.");
        }

        SceneEntity entity = sceneMapper.toEntity(request, companyId, userId);
        sceneRepository.save(entity);

        return ApiResponse.success("Scene added successfully.", Collections.emptyMap());
    }

    public ApiResponse<List<SceneResponse>> getAllScenes() {

        String userId = SecurityContextUtil.currentUserId();
        String companyId = SecurityContextUtil.currentCompanyId();

        if (userId == null || userId.isBlank() || companyId == null || companyId.isBlank()) {

            throw new UserNotValidException("Unauthorized user. Please Login again.");
        }
        List<SceneEntity> scenes = sceneRepository.findAllByUserIdAndCompanyId(userId, companyId);

        List<SceneResponse> res = scenes.stream()
                .map(sceneMapper::toResponse)
                .toList();

        return ApiResponse.success("Scenes fetched successfully.", res);
    }

    public ApiResponse<Map<Object, Object>> updateScene(
            String id,
            CreateSceneRequest request) {

        String userId = SecurityContextUtil.currentUserId();
        String companyId = SecurityContextUtil.currentCompanyId();

        if (userId == null || userId.isBlank()
                || companyId == null || companyId.isBlank()) {
            throw new UserNotValidException(
                    "Unauthorized user. Please Login again.");
        }

        SceneEntity scene = sceneRepository
                .findByIdAndUserIdAndCompanyId(id, userId, companyId)
                .orElseThrow(() -> new SceneNotFoundException("Scene not found."));

        Boolean exists = sceneRepository
                .existsByUserIdAndCompanyIdAndNameIgnoreCaseAndIdNot(
                        userId,
                        companyId,
                        request.getName(),
                        id);

        if (exists) {
            throw new SceneAlreadyExistsException(
                    "Scene with same name already exists.");
        }

        scene.setName(request.getName());
        scene.setDevices(request.getDevices());

        sceneRepository.save(scene);

        return ApiResponse.success(
                "Scene updated successfully.",
                Collections.emptyMap());
    }

    public ApiResponse<Map<Object, Object>> deleteScene(String id) {

        String userId = SecurityContextUtil.currentUserId();
        String companyId = SecurityContextUtil.currentCompanyId();

        if (userId == null || userId.isBlank()
                || companyId == null || companyId.isBlank()) {
            throw new UserNotValidException(
                    "Unauthorized user. Please Login again.");
        }

        SceneEntity scene = sceneRepository
                .findByIdAndUserIdAndCompanyId(id, userId, companyId)
                .orElseThrow(() -> new SceneNotFoundException("Scene not found."));

        sceneRepository.delete(scene);

        return ApiResponse.success(
                "Scene deleted successfully.",
                Collections.emptyMap());
    }

    public ApiResponse<Map<Object, Object>> executeScene(String id) {

        String userId = SecurityContextUtil.currentUserId();
        String companyId = SecurityContextUtil.currentCompanyId();

        if (userId == null || userId.isBlank()
                || companyId == null || companyId.isBlank()) {
            throw new UserNotValidException(
                    "Unauthorized user. Please Login again.");
        }

        SceneEntity scene = sceneRepository
                .findByIdAndUserIdAndCompanyId(id, userId, companyId)
                .orElseThrow(() -> new SceneNotFoundException("Scene not found."));

        if (scene.getDevices() == null || scene.getDevices().isEmpty()) {
            throw new SceneNotFoundException("No devices configured in scene.");
        }
        
        for (DeviceAction device : scene.getDevices()) {

            for (Action action : device.getActions()) {

                CommandDto command = new CommandDto();
                command.setCompanyId(companyId);
                command.setDeviceId(device.getDeviceId());
                command.setCmd(action.getCmd());
                command.setVal(action.getVal());

                kafkaProducerService.sendMessage(command);
            }
        }

        return ApiResponse.success(
                "Scene executed successfully.",
                Collections.emptyMap());
    }

}