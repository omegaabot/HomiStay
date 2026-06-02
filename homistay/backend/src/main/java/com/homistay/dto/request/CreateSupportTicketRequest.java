package com.homistay.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSupportTicketRequest {
    @NotBlank
    private String issueType;

    @NotBlank
    private String issueDescription;
}
