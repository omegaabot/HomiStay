package com.homistay.repository;

import com.homistay.entity.BookingExperience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingExperienceRepository extends JpaRepository<BookingExperience, Long> {
    Page<BookingExperience> findByGuestId(Long guestId, Pageable pageable);
}
