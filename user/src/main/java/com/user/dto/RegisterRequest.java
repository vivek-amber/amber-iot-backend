package com.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

    @NotBlank(message = "Company Id is required")
    String companyId,
             
    @NotBlank(message = "First Name is required")
    @Size(max = 50 )
    String firstName,
                    
    @NotBlank(message = "Last Name is required")
    @Size(max = 50)
    String lastName,
               
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    String email,
            
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 50)
    String password
) {
}