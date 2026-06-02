package com.homistay.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data @Builder
public class BookingAddonResponse {
    private Long id;
    private Long addonId;
    private String name;
    private String description;
    private int quantity;
    private BigDecimal price;
}
