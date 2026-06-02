package com.homistay.repository;

import com.homistay.entity.BookingModification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingModificationRepository extends JpaRepository<BookingModification, Long> {
    List<BookingModification> findByBookingIdOrderByCreatedAtDesc(Long bookingId);
    List<BookingModification> findByBookingPropertyHostIdAndStatusOrderByCreatedAtDesc(Long hostId, String status);
    List<BookingModification> findByBookingPropertyHostIdOrderByCreatedAtDesc(Long hostId);
}
