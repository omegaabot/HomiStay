package com.homistay.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageRequest {
    @NotBlank(message = "Message cannot be empty")
    private String message;
}