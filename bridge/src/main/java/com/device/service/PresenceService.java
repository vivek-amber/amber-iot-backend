package com.device.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.device.dto.DeviceStatusRequest;
import com.device.dto.StatusEvent;
import com.device.helpers.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresenceService {

        private final RedisTemplate<String, String> redisTemplate;

        public ApiResponse<StatusEvent> getStatus(
                        DeviceStatusRequest request) {

                String res = redisTemplate.opsForValue()
                                .get(buildKey(request.getCompanyId(), request.getDeviceId()));

                StatusEvent response = null;

                if (res == null) {

                        response = StatusEvent.builder()
                                        .deviceId(request.getDeviceId())
                                        .online(false)
                                        .build();
                }

                try {

                        response = StatusEvent.builder()
                                                .deviceId(request
                                                        .getDeviceId())
                                                .online(Boolean.parseBoolean(res))
                                                .build();
                } catch (Exception e) {
                                log.error("Error While parsing", e);
                }

                return ApiResponse.success("Status Fetched Successfully.", response);
        }

        private String buildKey(
                        String companyId,
                        String deviceId) {

                return String.format(
                                "presence:%s:%s",
                                companyId,
                                deviceId);
        }
}