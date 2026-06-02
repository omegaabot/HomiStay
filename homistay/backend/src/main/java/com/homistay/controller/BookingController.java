package com.homistay.controller;

import com.homistay.dto.request.BookingRequest;
import com.homistay.dto.request.MessageRequest;
import com.homistay.dto.response.BookingResponse;
import com.homistay.dto.response.MessageResponse;
import com.homistay.dto.response.PageResponse;
import com.homistay.service.BookingService;
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
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Create, cancel, and view bookings")
@SecurityRequirement(name = "bearerAuth")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Create a new booking (guest only)")
    public ResponseEntity<BookingResponse> create(
            @Valid @RequestBody BookingRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.createBooking(req, userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking details")
    public ResponseEntity<BookingResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bookingService.getBooking(id, userDetails.getUsername()));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel a booking")
    public ResponseEntity<BookingResponse> cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, userDetails.getUsername()));
    }

    @GetMapping("/my")
    @Operation(summary = "Get current guest's bookings (paginated)")
    public ResponseEntity<PageResponse<BookingResponse>> myBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bookingService.getGuestBookings(userDetails.getUsername(), page, size));
    }

    @PostMapping("/{id}/messages")
    @Operation(summary = "Send a message on a booking (guest or host)")
    public ResponseEntity<MessageResponse> sendMessage(
            @PathVariable Long id,
            @Valid @RequestBody MessageRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bookingService.sendMessage(id, req.getMessage(), userDetails.getUsername()));
    }

    @GetMapping("/{id}/messages")
    @Operation(summary = "Get all messages for a booking")
    public ResponseEntity<java.util.List<MessageResponse>> getMessages(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bookingService.getMessages(id, userDetails.getUsername()));
    }
}
