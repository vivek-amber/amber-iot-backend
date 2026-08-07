package com.user.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUser {
    
    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Boolean emailVerified;

    private LocalDateTime createdAt;
}
