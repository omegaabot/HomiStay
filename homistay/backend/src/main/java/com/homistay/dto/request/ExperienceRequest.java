package com.homistay.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ExperienceRequest {
    @NotBlank private String title;
    private String description;

    @NotNull @DecimalMin("1.00")
    private BigDecimal pricePerPerson;

    @NotNull @DecimalMin("0.5")
    private BigDecimal durationHours;

    @NotBlank private String city;

    @NotNull @Min(1)
    private Integer maxParticipants;
}
