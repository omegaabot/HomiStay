package com.homistay.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dynamic_pricing_configs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DynamicPricingConfig {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, unique = true)
    private Property property;

    private Boolean enabled;

    @Column(name = "min_price_multiplier", nullable = false)
    private BigDecimal minPriceMultiplier;

    @Column(name = "max_price_multiplier", nullable = false)
    private BigDecimal maxPriceMultiplier;

    @Column(name = "demand_threshold", nullable = false)
    private Integer demandThreshold;

    @Column(name = "lookback_months", nullable = false)
    private Integer lookbackMonths;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
