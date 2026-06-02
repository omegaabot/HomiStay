package com.homistay.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data @Builder
public class ExperienceResponse {
    private Long id;
    private Long hostId;
    private String hostName;
    private String title;
    private String description;
    private BigDecimal pricePerPerson;
    private BigDecimal durationHours;
    private String city;
    private Integer maxParticipants;
    private Boolean isActive;
}
