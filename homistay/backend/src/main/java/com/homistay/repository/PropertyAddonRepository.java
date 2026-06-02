package com.homistay.repository;

import com.homistay.entity.PropertyAddon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PropertyAddonRepository extends JpaRepository<PropertyAddon, Long> {
    List<PropertyAddon> findByPropertyIdAndIsActiveTrue(Long propertyId);
    List<PropertyAddon> findByPropertyId(Long propertyId);
    long countByPropertyId(Long propertyId);
}
