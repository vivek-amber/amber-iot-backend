package com.user.service;

import org.springframework.stereotype.Service;

import com.user.dto.ResponseUser;
import com.user.entity.UserEntity;
import com.user.exception.UserNotExistException;
import com.user.helper.ApiResponse;
import com.user.mapper.UserMapper;
import com.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper mapper;
    
    public ApiResponse<ResponseUser> fetchUserDetails(String uid) {
        
        UserEntity dbUser = userRepository.findById(uid).orElseThrow(()-> new UserNotExistException("No user found"));

        ResponseUser user = mapper.mapEntityToresponse(dbUser);

        return ApiResponse.success("Details fetched successfully.", user);
    }
}
