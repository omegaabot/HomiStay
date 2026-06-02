package com.homistay.controller;

import com.homistay.dto.response.SupportDashboardResponse;
import com.homistay.service.SupportTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/support/dashboard")
@RequiredArgsConstructor
@Tag(name = "Support Dashboard", description = "Support dashboard stats")
@SecurityRequirement(name = "bearerAuth")
public class SupportDashboardController {

    private final SupportTicketService supportTicketService;

    @GetMapping
    @Operation(summary = "Get support dashboard metrics")
    public ResponseEntity<SupportDashboardResponse> getStats() {
        return ResponseEntity.ok(supportTicketService.getDashboardStats());
    }
}
