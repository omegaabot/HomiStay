package com.homistay.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data @Builder
public class AddonResponse {
    private Long id;
    private Long propertyId;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean isActive;
}
