package com.homistay.controller;

import com.homistay.dto.request.ReviewRequest;
import com.homistay.dto.response.PageResponse;
import com.homistay.dto.response.ReviewResponse;
import com.homistay.service.ReviewService;
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
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Leave and view property reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Leave a review (guest, after completed stay)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ReviewResponse> create(
            @Valid @RequestBody ReviewRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.createReview(req, userDetails.getUsername()));
    }

    @GetMapping("/property/{propertyId}")
    @Operation(summary = "Get reviews for a property (paginated, public)")
    public ResponseEntity<PageResponse<ReviewResponse>> getPropertyReviews(
            @PathVariable Long propertyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.getPropertyReviews(propertyId, page, size));
    }
}
