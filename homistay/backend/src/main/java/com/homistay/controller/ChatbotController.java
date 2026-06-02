package com.homistay.controller;

import com.homistay.dto.response.ChatQueryResponse;
import com.homistay.dto.response.PageResponse;
import com.homistay.entity.User;
import com.homistay.repository.UserRepository;
import com.homistay.service.ChatQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
@Tag(name = "Chatbot", description = "Whirly chatbot endpoints")
public class ChatbotController {

    private final ChatQueryService chatQueryService;
    private final UserRepository userRepository;

    @PostMapping("/ask")
    @Operation(summary = "Ask Whirly a question")
    public ResponseEntity<Map<String, String>> ask(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> body) {
        String question = body.get("question");
        if (question == null || question.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Question is required"));
        }
        Long userId = null;
        String userName = "Guest";
        if (userDetails != null) {
            User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
            if (user != null) {
                userId = user.getId();
                userName = user.getFullName();
            }
        }
        String answer = chatQueryService.processQuestion(userId, userName, question);
        return ResponseEntity.ok(Map.of("answer", answer));
    }
}
