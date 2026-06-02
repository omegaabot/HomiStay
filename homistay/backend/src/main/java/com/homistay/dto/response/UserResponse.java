package com.homistay.dto.response;

import com.homistay.enums.Role;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String bio;
    private LocalDate dob;
    private String gender;
    private String avatarUrl;
    private Role role;
    private LocalDateTime createdAt;
}
