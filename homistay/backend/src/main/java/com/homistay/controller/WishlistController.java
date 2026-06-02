package com.homistay.controller;

import com.homistay.dto.response.PropertyResponse;
import com.homistay.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "Manage user wishlist / favorites")
@SecurityRequirement(name = "bearerAuth")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{propertyId}")
    @Operation(summary = "Add property to wishlist")
    public ResponseEntity<Void> add(
            @PathVariable Long propertyId,
            @AuthenticationPrincipal UserDetails userDetails) {
        wishlistService.addToWishlist(propertyId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{propertyId}")
    @Operation(summary = "Remove property from wishlist")
    public ResponseEntity<Void> remove(
            @PathVariable Long propertyId,
            @AuthenticationPrincipal UserDetails userDetails) {
        wishlistService.removeFromWishlist(propertyId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Get user's wishlist properties")
    public ResponseEntity<List<PropertyResponse>> getWishlist(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(wishlistService.getWishlist(userDetails.getUsername()));
    }
}
