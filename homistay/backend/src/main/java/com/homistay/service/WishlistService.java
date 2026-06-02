package com.homistay.service;

import com.homistay.dto.response.PropertyResponse;
import com.homistay.entity.Property;
import com.homistay.entity.User;
import com.homistay.entity.Wishlist;
import com.homistay.exception.BusinessException;
import com.homistay.exception.ResourceNotFoundException;
import com.homistay.repository.PropertyRepository;
import com.homistay.repository.UserRepository;
import com.homistay.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final PropertyService propertyService;

    @Transactional
    public void addToWishlist(Long propertyId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property", propertyId));

        if (wishlistRepository.existsByUserIdAndPropertyId(user.getId(), property.getId())) {
            throw new BusinessException("Property is already in wishlist");
        }

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .property(property)
                .build();
        wishlistRepository.save(wishlist);
    }

    @Transactional
    public void removeFromWishlist(Long propertyId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        wishlistRepository.deleteByUserIdAndPropertyId(user.getId(), propertyId);
    }

    @Transactional(readOnly = true)
    public List<PropertyResponse> getWishlist(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return wishlistRepository.findByUserId(user.getId()).stream()
                .map(w -> propertyService.mapToResponse(w.getProperty()))
                .collect(Collectors.toList());
    }
}
