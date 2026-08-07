package com.user.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.user.dto.RegisterRequest;
import com.user.entity.UserEntity;
import com.user.dto.ResponseUser;

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
    
    public ResponseUser mapEntityToresponse( UserEntity user) {

        return ResponseUser.builder()
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .emailVerified(user.getEmailVerified())
        .createdAt(user.getCreatedAt())
        .build();
    }
}
