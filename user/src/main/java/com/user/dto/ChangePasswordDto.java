package com.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDto(

    @NotBlank(message = "New Password is required")
    @Size(min = 8, max = 50)
    String currentPassword,
        
    @NotBlank(message = "New Password is required")
    @Size(min = 8, max = 50)
    String newPassword

) {
    
}
