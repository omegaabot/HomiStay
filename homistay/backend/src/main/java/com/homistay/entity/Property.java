package com.homistay.entity;

import com.homistay.enums.PropertyType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "properties")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Property {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private User host;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType type;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    private String address;

    private Double latitude;
    private Double longitude;

    @Column(name = "cleaning_fee", precision = 10, scale = 2)
    @Builder.Default private java.math.BigDecimal cleaningFee = java.math.BigDecimal.ZERO;

    @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(name = "max_guests", nullable = false)
    @Builder.Default private Integer maxGuests = 1;

    @Column(nullable = false)
    @Builder.Default private Integer bedrooms = 1;

    @Column(nullable = false)
    @Builder.Default private Integer bathrooms = 1;

    @Column(columnDefinition = "TEXT")
    private String amenities;

    @Column(name = "allows_children", nullable = false, columnDefinition = "boolean default true")
    @Builder.Default private Boolean allowsChildren = true;

    @Column(name = "allows_infants", nullable = false, columnDefinition = "boolean default true")
    @Builder.Default private Boolean allowsInfants = true;

    @Column(name = "allows_pets", nullable = false, columnDefinition = "boolean default false")
    @Builder.Default private Boolean allowsPets = false;

    @Column(name = "is_active", nullable = false)
    @Builder.Default private Boolean isActive = true;

    @Column(name = "house_rules", columnDefinition = "TEXT")
    private String houseRules;

    @Column(name = "guest_requirements", columnDefinition = "TEXT")
    private String guestRequirements;

    @Column(name = "check_in_instructions", columnDefinition = "TEXT")
    private String checkInInstructions;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<PropertyImage> images;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Availability> availabilities;
}
