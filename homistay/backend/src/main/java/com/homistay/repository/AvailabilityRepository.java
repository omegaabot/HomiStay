package com.homistay.repository;

import com.homistay.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    List<Availability> findByPropertyIdAndDateBetween(Long propertyId, LocalDate from, LocalDate to);

    @Modifying
    @Query("DELETE FROM Availability a WHERE a.property.id = :propertyId AND a.date BETWEEN :from AND :to")
    void deleteByPropertyIdAndDateBetween(@Param("propertyId") Long propertyId,
                                          @Param("from") LocalDate from,
                                          @Param("to") LocalDate to);

    @Query("SELECT a.date FROM Availability a WHERE a.property.id = :propertyId AND a.isAvailable = false")
    List<LocalDate> findBlockedDatesByPropertyId(@Param("propertyId") Long propertyId);

    @Modifying
    @Query("DELETE FROM Availability a WHERE a.property.id = :propertyId AND a.reason = 'HOST_BLOCKED' AND a.date BETWEEN :from AND :to")
    void deleteHostBlocksByPropertyIdAndDateBetween(@Param("propertyId") Long propertyId,
                                                    @Param("from") LocalDate from,
                                                    @Param("to") LocalDate to);
}
