package com.homistay.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder
public class ModificationResponse {
    private Long id;
    private Long bookingId;
    private Long requestedBy;
    private String requestedByName;
    private LocalDate newCheckIn;
    private LocalDate newCheckOut;
    private Integer newGuests;
    private String reason;
    private String status;
    private String hostResponse;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
}
