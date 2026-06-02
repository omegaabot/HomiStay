package com.homistay.service;

import com.homistay.dto.response.ChatQueryResponse;
import com.homistay.dto.response.PageResponse;
import com.homistay.entity.ChatQuery;
import com.homistay.repository.ChatQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatQueryService {

    private final ChatQueryRepository chatQueryRepository;
    private final FaqService faqService;

    public String processQuestion(Long userId, String userName, String question) {
        String answer = faqService.findAnswer(question)
            .orElse("I couldn't find an answer for that question. Please contact our Support Team for further assistance.\n\n📧 support@homistay.com\n\nOur support team will help you resolve your issue.");

        ChatQuery chatQuery = ChatQuery.builder()
            .userId(userId)
            .userName(userName)
            .question(question)
            .chatbotResponse(answer)
            .build();
        chatQueryRepository.save(chatQuery);
        return answer;
    }

    public PageResponse<ChatQueryResponse> getAllQueries(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatQuery> queryPage = chatQueryRepository.findAll(pageable);
        return toPageResponse(queryPage);
    }

    public PageResponse<ChatQueryResponse> searchQueries(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatQuery> queryPage = chatQueryRepository.findByQuestionContainingIgnoreCase(keyword, pageable);
        return toPageResponse(queryPage);
    }

    public List<ChatQueryResponse> getRecentQueries() {
        return chatQueryRepository.findTop10ByOrderByCreatedAtDesc().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public long getTotalQueries() {
        return chatQueryRepository.count();
    }

    private PageResponse<ChatQueryResponse> toPageResponse(Page<ChatQuery> page) {
        List<ChatQueryResponse> content = page.getContent().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return PageResponse.<ChatQueryResponse>builder()
            .content(content)
            .page(page.getNumber())
            .size(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .last(page.isLast())
            .build();
    }

    private ChatQueryResponse toResponse(ChatQuery q) {
        return ChatQueryResponse.builder()
            .id(q.getId())
            .userId(q.getUserId())
            .userName(q.getUserName())
            .question(q.getQuestion())
            .chatbotResponse(q.getChatbotResponse())
            .createdAt(q.getCreatedAt())
            .build();
    }
}
