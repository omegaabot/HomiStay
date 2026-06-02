package com.homistay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_queries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatQuery {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name = "chatbot_response", nullable = false, columnDefinition = "TEXT")
    private String chatbotResponse;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
