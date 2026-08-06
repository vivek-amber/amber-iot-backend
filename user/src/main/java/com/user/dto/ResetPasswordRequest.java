package com.user.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
    @NotBlank(message = "Email is required")
    String email,
                    
    @NotBlank(message = "Company Id is required")
    String companyId,
                    
    @NotBlank(message = "Otp is required")
    String otp,
                    
    @NotBlank(message = "Password is required")
    String newPassword
) {
}