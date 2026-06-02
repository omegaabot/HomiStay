package com.homistay.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank
    private String currentPassword;
    
    @NotBlank
    @jakarta.validation.constraints.Size(min = 8, message = "New password must be at least 8 characters")
    private String newPassword;
}
