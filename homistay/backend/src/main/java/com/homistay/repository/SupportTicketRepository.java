package com.homistay.repository;

import com.homistay.entity.SupportTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    Page<SupportTicket> findByUserId(Long userId, Pageable pageable);
    Page<SupportTicket> findByAssignedToId(Long assignedToId, Pageable pageable);
    Page<SupportTicket> findByStatus(String status, Pageable pageable);
    List<SupportTicket> findTop10ByOrderByCreatedAtDesc();
}
