package com.homistay.repository;

import com.homistay.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);
    Optional<Wishlist> findByUserIdAndPropertyId(Long userId, Long propertyId);
    boolean existsByUserIdAndPropertyId(Long userId, Long propertyId);

    @Modifying
    @Query("DELETE FROM Wishlist w WHERE w.user.id = :userId AND w.property.id = :propertyId")
    void deleteByUserIdAndPropertyId(@Param("userId") Long userId, @Param("propertyId") Long propertyId);
}
