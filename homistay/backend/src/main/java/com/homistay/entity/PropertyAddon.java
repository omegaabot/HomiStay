package com.homistay.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "property_addons")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PropertyAddon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "is_active", nullable = false)
    @Builder.Default private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    @Builder.Default private LocalDateTime createdAt = LocalDateTime.now();
}
