package com.homistay.repository;

import com.homistay.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByBookingId(Long bookingId);

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM Payment p
        WHERE p.booking.property.host.id = :hostId
          AND p.status = 'PAID'
    """)
    BigDecimal totalEarningsByHostId(@Param("hostId") Long hostId);

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM Payment p
        WHERE p.booking.property.host.id = :hostId
          AND p.status = 'PAID'
          AND p.paidAt >= :since
    """)
    BigDecimal earningsSince(@Param("hostId") Long hostId, @Param("since") java.time.LocalDateTime since);
}
