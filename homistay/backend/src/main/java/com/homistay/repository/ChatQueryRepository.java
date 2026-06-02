package com.homistay.repository;

import com.homistay.entity.ChatQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatQueryRepository extends JpaRepository<ChatQuery, Long> {
    Page<ChatQuery> findByQuestionContainingIgnoreCase(String keyword, Pageable pageable);
    List<ChatQuery> findTop10ByOrderByCreatedAtDesc();
}
