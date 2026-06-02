package com.homistay.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class DynamicPricingConfigResponse {
    private Long id;
    private Long propertyId;
    private Boolean enabled;
    private BigDecimal minPriceMultiplier;
    private BigDecimal maxPriceMultiplier;
    private Integer demandThreshold;
    private Integer lookbackMonths;
    private LocalDateTime updatedAt;
}
