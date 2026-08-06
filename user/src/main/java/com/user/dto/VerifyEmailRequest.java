package com.user.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyEmailRequest(
    @NotBlank(message = "Company Id is required")
    String companyId,
                
    @NotBlank(message = "Email is required")
    String email,
                    
    @NotBlank(message = "Otp is required")
    String otp
) {
}