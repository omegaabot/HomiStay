package com.homistay.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "experiences")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Experience {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private User host;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "price_per_person", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerPerson;

    @Column(name = "duration_hours", nullable = false, precision = 4, scale = 1)
    private BigDecimal durationHours;

    @Column(nullable = false)
    private String city;

    @Column(name = "max_participants", nullable = false)
    @Builder.Default private Integer maxParticipants = 10;

    @Column(name = "is_active", nullable = false)
    @Builder.Default private Boolean isActive = true;
}
