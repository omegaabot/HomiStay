package com.homistay.controller;

import com.homistay.dto.request.BookingExperienceRequest;
import com.homistay.dto.request.ExperienceRequest;
import com.homistay.dto.response.ExperienceResponse;
import com.homistay.dto.response.PageResponse;
import com.homistay.service.ExperienceService;
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
@RequestMapping("/api/experiences")
@RequiredArgsConstructor
@Tag(name = "Experiences", description = "Browse and book local experiences")
public class ExperienceController {

    private final ExperienceService experienceService;

    @GetMapping
    @Operation(summary = "List experiences (optionally filter by city)")
    public ResponseEntity<PageResponse<ExperienceResponse>> list(
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(experienceService.listAll(city, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get experience details")
    public ResponseEntity<ExperienceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(experienceService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create an experience (HOST only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ExperienceResponse> create(
            @Valid @RequestBody ExperienceRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(experienceService.create(req, userDetails.getUsername()));
    }

    @PostMapping("/book")
    @Operation(summary = "Book an experience (GUEST only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> book(
            @Valid @RequestBody BookingExperienceRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        experienceService.bookExperience(req, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
