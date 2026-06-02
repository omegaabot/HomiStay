package com.homistay.repository;

import com.homistay.entity.Property;
import com.homistay.enums.PropertyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    Page<Property> findByHostIdAndIsActiveTrue(Long hostId, Pageable pageable);

    Page<Property> findByHostId(Long hostId, Pageable pageable);

    /** Search with availability check — excludes properties with any CONFIRMED/PENDING
        booking that overlaps the requested dates */
    @Query("""
        SELECT p FROM Property p
        WHERE p.isActive = true
          AND (CAST(:city AS string) IS NULL OR p.city ILIKE CONCAT('%', CAST(:city AS string), '%'))
          AND (:type IS NULL OR p.type = :type)
          AND (:minPrice IS NULL OR p.pricePerNight >= :minPrice)
          AND (:maxPrice IS NULL OR p.pricePerNight <= :maxPrice)
          AND (:guests IS NULL OR p.maxGuests >= :guests)
          AND (
              :checkIn IS NULL OR :checkOut IS NULL
              OR NOT EXISTS (
                  SELECT b FROM Booking b
                  WHERE b.property = p
                    AND b.status IN ('PENDING','CONFIRMED')
                    AND b.checkIn < :checkOut
                    AND b.checkOut > :checkIn
              )
          )
    """)
    Page<Property> searchAvailable(
        @Param("city")      String city,
        @Param("type")      PropertyType type,
        @Param("minPrice")  BigDecimal minPrice,
        @Param("maxPrice")  BigDecimal maxPrice,
        @Param("guests")    Integer guests,
        @Param("checkIn")   LocalDate checkIn,
        @Param("checkOut")  LocalDate checkOut,
        Pageable pageable
    );

    List<Property> findByHostIdAndIsActiveTrue(Long hostId);
}
