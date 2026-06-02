package com.homistay.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class ChatQueryResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String question;
    private String chatbotResponse;
    private LocalDateTime createdAt;
}
