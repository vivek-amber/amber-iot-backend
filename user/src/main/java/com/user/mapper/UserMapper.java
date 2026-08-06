package com.user.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.user.dto.RegisterRequest;
import com.user.entity.UserEntity;

@Component
public class UserMapper {
    

    public UserEntity mapRegisterRequestToEntity(RegisterRequest request, String password) {

        return UserEntity.builder()
        .firstName(request.firstName().trim())
        .lastName(request.lastName().trim())
        .email(request.email().trim())
        .companyId(request.companyId().trim())
        .roles(List.of("member"))
        .password(password)
        .build();
    }
}
