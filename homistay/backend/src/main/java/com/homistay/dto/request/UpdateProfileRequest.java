package com.homistay.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private String phone;
    private String bio;
    private LocalDate dob;
    private String gender;
    private String avatarUrl;
}
