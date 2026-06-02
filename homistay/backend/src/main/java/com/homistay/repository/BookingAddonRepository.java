package com.homistay.repository;

import com.homistay.entity.BookingAddon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingAddonRepository extends JpaRepository<BookingAddon, Long> {
    List<BookingAddon> findByBookingId(Long bookingId);
    void deleteByBookingId(Long bookingId);
}
