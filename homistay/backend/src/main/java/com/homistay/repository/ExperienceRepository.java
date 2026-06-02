package com.homistay.repository;

import com.homistay.entity.Experience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    Page<Experience> findByIsActiveTrue(Pageable pageable);
    Page<Experience> findByCityIgnoreCaseAndIsActiveTrue(String city, Pageable pageable);
    Page<Experience> findByHostIdAndIsActiveTrue(Long hostId, Pageable pageable);
}
