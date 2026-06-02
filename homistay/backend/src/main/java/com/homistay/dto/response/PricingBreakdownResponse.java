package com.homistay.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data @Builder
public class PricingBreakdownResponse {
    private BigDecimal basePricePerNight;
    private Integer nights;
    private BigDecimal baseTotal;
    private BigDecimal seasonalMultiplier;
    private BigDecimal demandMultiplier;
    private BigDecimal effectivePricePerNight;
    private BigDecimal subtotal;
    private BigDecimal cleaningFee;
    private BigDecimal addOnsTotal;
    private BigDecimal total;
    private List<Map<String, Object>> nightlyBreakdown;
    private String seasonName;
}
