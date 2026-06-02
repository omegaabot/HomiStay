package com.homistay.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "seasonal_rates")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SeasonalRate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "price_multiplier", nullable = false)
    private BigDecimal priceMultiplier;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
