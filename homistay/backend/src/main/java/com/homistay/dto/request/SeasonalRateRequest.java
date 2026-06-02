package com.homistay.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SeasonalRateRequest {
    private Long propertyId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal priceMultiplier;
    private Boolean isActive;
}
