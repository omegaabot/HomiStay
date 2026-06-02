package com.homistay.entity;

import com.homistay.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Booking {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private User guest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @Column(name = "guests_count", nullable = false)
    @Builder.Default private Integer guestsCount = 1;

    @Column(nullable = false, columnDefinition = "integer default 1")
    @Builder.Default private Integer adults = 1;

    @Column(nullable = false, columnDefinition = "integer default 0")
    @Builder.Default private Integer children = 0;

    @Column(nullable = false, columnDefinition = "integer default 0")
    @Builder.Default private Integer infants = 0;

    @Column(nullable = false, columnDefinition = "integer default 0")
    @Builder.Default private Integer pets = 0;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default private BookingStatus status = BookingStatus.PENDING;

    @Column(name = "special_requests", columnDefinition = "TEXT")
    private String specialRequests;

    @Column(name = "special_request_status")
    @Builder.Default private String specialRequestStatus = "PENDING";

    @Column(name = "host_notes", columnDefinition = "TEXT")
    private String hostNotes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Review review;
}
