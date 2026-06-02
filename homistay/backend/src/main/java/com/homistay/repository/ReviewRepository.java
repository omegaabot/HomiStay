package com.homistay.repository;

import com.homistay.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByPropertyId(Long propertyId, Pageable pageable);

    boolean existsByBookingId(Long bookingId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.property.id = :propertyId")
    Double averageRatingByPropertyId(@Param("propertyId") Long propertyId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.property.id IN :propertyIds")
    Double averageRatingByPropertyIds(@Param("propertyIds") java.util.List<Long> propertyIds);
}
