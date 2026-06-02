package com.homistay.controller;

import com.homistay.dto.response.ChatQueryResponse;
import com.homistay.dto.response.PageResponse;
import com.homistay.service.ChatQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support/queries")
@RequiredArgsConstructor
@Tag(name = "Support Queries", description = "Support logs for chatbot queries")
@SecurityRequirement(name = "bearerAuth")
public class SupportQueryController {

    private final ChatQueryService chatQueryService;

    @GetMapping
    @Operation(summary = "Get all chatbot query logs")
    public ResponseEntity<PageResponse<ChatQueryResponse>> getAllQueries(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (keyword != null && !keyword.isBlank()) {
            return ResponseEntity.ok(chatQueryService.searchQueries(keyword, page, size));
        }
        return ResponseEntity.ok(chatQueryService.getAllQueries(page, size));
    }

    @GetMapping("/recent")
    @Operation(summary = "Get top 10 recent queries")
    public ResponseEntity<List<ChatQueryResponse>> getRecent() {
        return ResponseEntity.ok(chatQueryService.getRecentQueries());
    }
}
