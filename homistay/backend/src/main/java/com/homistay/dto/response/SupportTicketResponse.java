package com.homistay.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class SupportTicketResponse {
    private Long id;
    private Long userId;
    private String userEmail;
    private String userName;
    private String issueType;
    private String issueDescription;
    private String status;
    private Long assignedToId;
    private String assignedToName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
