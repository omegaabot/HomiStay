package com.homistay.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "availability", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"property_id", "date"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Availability {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "is_available", nullable = false)
    @Builder.Default private Boolean isAvailable = false;

    private String reason;
}
