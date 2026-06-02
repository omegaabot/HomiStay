package com.homistay.repository;

import com.homistay.entity.DynamicPricingConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DynamicPricingConfigRepository extends JpaRepository<DynamicPricingConfig, Long> {
    Optional<DynamicPricingConfig> findByPropertyId(Long propertyId);
}
