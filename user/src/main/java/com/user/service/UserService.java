package com.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sucurity.context.SecurityContextUtil;
import com.user.dto.ResponseUser;
import com.user.dto.SessionResponseDto;
import com.user.entity.UserEntity;
import com.user.entity.UserSessionEntity;
import com.user.exception.UserNotExistException;
import com.user.helper.ApiResponse;
import com.user.mapper.UserMapper;
import com.user.repository.SessionRepository;
import com.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;

    private final UserMapper mapper;


    public ApiResponse<ResponseUser> fetchUserDetails(String uid) {

        UserEntity dbUser = userRepository.findById(uid).orElseThrow(() -> new UserNotExistException("No user found"));

        ResponseUser user = mapper.mapEntityToresponse(dbUser);

        return ApiResponse.success("Details fetched successfully.", user);
    }

    public ApiResponse<List<SessionResponseDto>> getAllSessions() {

        try {

            String userId = SecurityContextUtil.currentUserId();
            String currentSessionId = SecurityContextUtil.currentSessionId();

            List<SessionResponseDto> sessions = sessionRepository.findByUserId(userId)
                    .stream()
                    .map(session -> SessionResponseDto.builder()
                            .id(session.getId())
                            .active(session.getActive())
                            .deviceName(
                                    buildDeviceName(
                                            session,
                                            currentSessionId))
                            .build())
                    .toList();

            return ApiResponse.success(
                    "Sessions fetched successfully",
                    sessions);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ApiResponse.success(
                "Sessions fetched successfully",
                null);
    }

    private String buildDeviceName(
            UserSessionEntity session,
            String currentSessionId) {

        if (session.getId().equals(currentSessionId)) {
            return session.getDeviceName() + " (Current Device)";
        }

        return session.getDeviceName();
    }
}
