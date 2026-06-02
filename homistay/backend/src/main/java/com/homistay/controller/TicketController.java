package com.homistay.controller;

import com.homistay.dto.request.CreateSupportTicketRequest;
import com.homistay.dto.response.PageResponse;
import com.homistay.dto.response.SupportTicketResponse;
import com.homistay.entity.User;
import com.homistay.repository.UserRepository;
import com.homistay.service.SupportTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Guest support ticket endpoints")
@SecurityRequirement(name = "bearerAuth")
public class TicketController {

    private final SupportTicketService supportTicketService;
    private final UserRepository userRepository;

    @PostMapping
    @Operation(summary = "Create a support ticket")
    public ResponseEntity<SupportTicketResponse> createTicket(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateSupportTicketRequest request) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(supportTicketService.createTicket(user.getId(), request));
    }

    @GetMapping
    @Operation(summary = "Get guest's own support tickets")
    public ResponseEntity<PageResponse<SupportTicketResponse>> getMyTickets(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(supportTicketService.getUserTickets(user.getId(), page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket details by ID")
    public ResponseEntity<SupportTicketResponse> getTicketById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        SupportTicketResponse response = supportTicketService.getTicketById(id);
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        if (!response.getUserId().equals(user.getId()) &&
                !user.getRole().name().equals("SUPPORT_TEAM") &&
                !user.getRole().name().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(response);
    }
}
