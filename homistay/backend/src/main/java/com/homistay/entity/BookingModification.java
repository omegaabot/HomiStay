package com.homistay.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking_modifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookingModification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by", nullable = false)
    private User requestedBy;

    @Column(name = "new_check_in")
    private LocalDate newCheckIn;

    @Column(name = "new_check_out")
    private LocalDate newCheckOut;

    @Column(name = "new_guests")
    private Integer newGuests;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(nullable = false)
    @Builder.Default private String status = "PENDING";

    @Column(name = "host_response", columnDefinition = "TEXT")
    private String hostResponse;

    @Column(name = "created_at", updatable = false)
    @Builder.Default private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
}
