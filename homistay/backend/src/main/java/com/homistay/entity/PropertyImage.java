package com.homistay.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "property_images")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PropertyImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(name = "is_primary", nullable = false)
    @Builder.Default private Boolean isPrimary = false;

    @Column(name = "display_order", nullable = false)
    @Builder.Default private Integer displayOrder = 0;
}
