package com.homistay.repository;

import com.homistay.entity.SeasonalRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SeasonalRateRepository extends JpaRepository<SeasonalRate, Long> {

    List<SeasonalRate> findByPropertyId(Long propertyId);

    @Query("SELECT s FROM SeasonalRate s WHERE s.property.id = :propertyId AND s.isActive = true AND s.startDate <= :date AND s.endDate >= :date")
    List<SeasonalRate> findActiveByPropertyIdAndDate(@Param("propertyId") Long propertyId, @Param("date") LocalDate date);

    @Query("SELECT s FROM SeasonalRate s WHERE s.property.id = :propertyId AND s.isActive = true AND s.startDate <= :endDate AND s.endDate >= :startDate")
    List<SeasonalRate> findActiveByPropertyIdAndDateRange(@Param("propertyId") Long propertyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    void deleteByPropertyId(Long propertyId);
}
