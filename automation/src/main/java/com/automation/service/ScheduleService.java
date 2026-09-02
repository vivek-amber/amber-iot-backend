package com.automation.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.automation.dto.CreateScheduleRequest;
import com.automation.dto.ScheduleResponse;
import com.automation.entity.ScheduleEntity;
import com.automation.exception.ScheduleNotFoundException;
import com.automation.exception.UserNotValidException;
import com.automation.mapper.ScheduleMapper;
import com.automation.repository.ScheduleRepository;
import com.automation.utils.ApiResponse;
import com.sucurity.context.SecurityContextUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleMapper scheduleMapper;
    
    private final ScheduleRepository scheduleRepository;

    private final QuartzSchedulerService quartzSchedulerService;

    public ApiResponse<Map<Object, Object>> createSchedule(CreateScheduleRequest request) {

        String userId = SecurityContextUtil.currentUserId();
        String companyId = SecurityContextUtil.currentCompanyId();

        if (userId == null || userId.isBlank() || companyId == null || companyId.isBlank()) {

            throw new UserNotValidException("Unauthorized user. Please Login again.");
        }

        ScheduleEntity entity = scheduleMapper.toEntity(request, companyId, userId);
        ScheduleEntity saved = scheduleRepository.save(entity);

        quartzSchedulerService.createJob(saved);
        return ApiResponse.success("Schedule added successfully.", Collections.emptyMap());
    }

    public ApiResponse<List<ScheduleResponse>> getAllSchedules() {

        String userId = SecurityContextUtil.currentUserId();
        String companyId = SecurityContextUtil.currentCompanyId();

        if (userId == null || userId.isBlank() || companyId == null || companyId.isBlank()) {

            throw new UserNotValidException("Unauthorized user. Please Login again.");
        }

        List<ScheduleEntity> schedules = scheduleRepository.findAllByUserIdAndCompanyId(userId, companyId);

        List<ScheduleResponse> res = schedules.stream()
                .map(scheduleMapper::toResponse)
                .toList();

        return ApiResponse.success("Schedules fetched successfully.", res);
    }

    public ApiResponse<Map<Object, Object>> toggleSchedule(String id) {

        String userId = SecurityContextUtil.currentUserId();
        String companyId = SecurityContextUtil.currentCompanyId();

        if (userId == null || userId.isBlank() || companyId == null || companyId.isBlank()) {

            throw new UserNotValidException("Unauthorized user. Please Login again.");
        }

        Optional<ScheduleEntity> schedule = scheduleRepository.findById(id);

        if (schedule.isEmpty()) {

            throw new ScheduleNotFoundException("Schedule not found.");
        }

        ScheduleEntity scheduleEntity = schedule.get();
        scheduleEntity.setEnabled(!scheduleEntity.getEnabled());

        ScheduleEntity saved = scheduleRepository.save(scheduleEntity);

        if (saved.getEnabled()) {
            quartzSchedulerService.createJob(saved);
        } else {
            quartzSchedulerService.deleteJob(saved.getId());
        }
        return ApiResponse.success("Schedule updated successfully.", Collections.emptyMap());
    }

    public ApiResponse<Map<Object, Object>> updateSchedule(String id, CreateScheduleRequest request) {

        String userId = SecurityContextUtil.currentUserId();
        String companyId = SecurityContextUtil.currentCompanyId();

        if (userId == null || userId.isBlank() || companyId == null || companyId.isBlank()) {

            throw new UserNotValidException("Unauthorized user. Please Login again.");
        }

        Optional<ScheduleEntity> schedule = scheduleRepository.findById(id);

        if (schedule.isEmpty()) {

            throw new ScheduleNotFoundException("Schedule not found.");
        }

        ScheduleEntity scheduleEntity = schedule.get();
        scheduleEntity.setEnabled(request.getEnabled());
        scheduleEntity.setDevice(request.getDevice());
        scheduleEntity.setName(request.getName());
        scheduleEntity.setScheduleTrigger(request.getScheduleTrigger());
        scheduleEntity.setUpdatedAt(Instant.now());

        ScheduleEntity saved = scheduleRepository.save(scheduleEntity);

        // first delete old 
        quartzSchedulerService.deleteJob(saved.getId());

        // create new 
        quartzSchedulerService.createJob(saved);

        return ApiResponse.success("Schedule updated successfully.", Collections.emptyMap());
    }

    public ApiResponse<Map<Object, Object>> deleteSchedule(String id) {

        String userId = SecurityContextUtil.currentUserId();
        String companyId = SecurityContextUtil.currentCompanyId();

        if (userId == null || userId.isBlank() || companyId == null || companyId.isBlank()) {

            throw new UserNotValidException("Unauthorized user. Please Login again.");
        }

        scheduleRepository.deleteById(id);
        return ApiResponse.success("Schedule updated successfully.", Collections.emptyMap());
    }

}
