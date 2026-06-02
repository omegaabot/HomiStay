package com.homistay.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data @Builder
public class HostDashboardResponse {
    private Integer totalProperties;
    private Integer activeProperties;
    private Integer totalBookings;
    private Integer pendingBookings;
    private Integer confirmedBookings;
    private BigDecimal totalEarnings;
    private BigDecimal monthlyEarnings;
    private Double averageRating;
    private List<BookingResponse> recentBookings;
}
