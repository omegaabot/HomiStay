package com.homistay.dto.request;

import com.homistay.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank
    private String fullName;

    private String phone;

    @NotNull
    private Role role; // GUEST or HOST
}
