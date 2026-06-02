package com.homistay.controller;

import com.homistay.dto.request.ModificationRequest;
import com.homistay.dto.response.ModificationResponse;
import com.homistay.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ModificationController {

    private final BookingService bookingService;

    @PostMapping("/{id}/modification")
    @Operation(summary = "Guest requests a modification to their booking")
    public ResponseEntity<ModificationResponse> requestModification(
            @PathVariable Long id,
            @Valid @RequestBody ModificationRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bookingService.requestModification(id, req, userDetails.getUsername()));
    }

    @GetMapping("/{id}/modifications")
    @Operation(summary = "View modification history for a booking")
    public ResponseEntity<List<ModificationResponse>> getModifications(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bookingService.getModificationsForBooking(id, userDetails.getUsername()));
    }
}
