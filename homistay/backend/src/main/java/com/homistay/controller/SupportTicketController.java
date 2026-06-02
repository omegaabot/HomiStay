package com.homistay.controller;

import com.homistay.dto.request.UpdateTicketStatusRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/support/tickets")
@RequiredArgsConstructor
@Tag(name = "Support Tickets", description = "Support staff ticket management")
@SecurityRequirement(name = "bearerAuth")
public class SupportTicketController {

    private final SupportTicketService supportTicketService;
    private final UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Get all tickets (support / admin)")
    public ResponseEntity<PageResponse<SupportTicketResponse>> getAllTickets(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (status != null && !status.isBlank()) {
            return ResponseEntity.ok(supportTicketService.getTicketsByStatus(status, page, size));
        }
        return ResponseEntity.ok(supportTicketService.getAllTickets(page, size));
    }

    @PatchMapping("/{id}/assign")
    @Operation(summary = "Assign ticket to support agent")
    public ResponseEntity<SupportTicketResponse> assignTicket(
            @PathVariable Long id,
            @RequestParam Long agentId) {
        return ResponseEntity.ok(supportTicketService.assignTicket(id, agentId));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update ticket status")
    public ResponseEntity<SupportTicketResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketStatusRequest request) {
        return ResponseEntity.ok(supportTicketService.updateTicketStatus(id, request));
    }

    @GetMapping("/my-assigned")
    @Operation(summary = "Get tickets assigned to logged-in support agent")
    public ResponseEntity<PageResponse<SupportTicketResponse>> getMyAssignedTickets(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(supportTicketService.getAssignedTickets(user.getId(), page, size));
    }
}
