package com.homistay.controller;

import com.homistay.dto.request.PropertyRequest;
import com.homistay.dto.request.PropertySearchRequest;
import java.util.List;
import java.time.LocalDate;
import com.homistay.dto.response.PageResponse;
import com.homistay.dto.response.PropertyResponse;
import com.homistay.service.PropertyService;
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
@RequestMapping("/api/properties")
@RequiredArgsConstructor
@Tag(name = "Properties", description = "Search, view, and manage property listings")
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping
    @Operation(summary = "Search available properties with filters")
    public ResponseEntity<PageResponse<PropertyResponse>> search(PropertySearchRequest req) {
        return ResponseEntity.ok(propertyService.search(req));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get property details by ID")
    public ResponseEntity<PropertyResponse> getById(@PathVariable Long id,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(propertyService.getById(id, email));
    }

    @GetMapping("/{id}/blocked-dates")
    @Operation(summary = "Get all blocked / unavailable dates for a property")
    public ResponseEntity<List<LocalDate>> getBlockedDates(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.getBlockedDates(id));
    }

    @PostMapping
    @Operation(summary = "Create a property (HOST only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PropertyResponse> create(
            @Valid @RequestBody PropertyRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(propertyService.create(req, userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a property (owner only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PropertyResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PropertyRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(propertyService.update(id, req, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a property (owner only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        propertyService.delete(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
