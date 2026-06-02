package com.homistay.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTicketStatusRequest {
    @NotBlank
    private String status;
}
