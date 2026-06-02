package com.homistay.service;

import com.homistay.dto.request.ReviewRequest;
import com.homistay.dto.response.PageResponse;
import com.homistay.dto.response.ReviewResponse;
import com.homistay.entity.*;
import com.homistay.enums.BookingStatus;
import com.homistay.exception.BusinessException;
import com.homistay.exception.ResourceNotFoundException;
import com.homistay.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponse createReview(ReviewRequest req, String guestEmail) {
        Booking booking = bookingRepository.findById(java.util.Objects.requireNonNull(req.getBookingId()))
                .orElseThrow(() -> new ResourceNotFoundException("Booking", req.getBookingId()));

        if (!booking.getGuest().getEmail().equals(guestEmail)) {
            throw new BusinessException("You can only review your own bookings");
        }
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new BusinessException("Can only review completed stays");
        }
        if (reviewRepository.existsByBookingId(req.getBookingId())) {
            throw new BusinessException("You have already reviewed this booking");
        }

        User reviewer = userRepository.findByEmail(guestEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Review review = Review.builder()
                .booking(booking).reviewer(reviewer)
                .property(booking.getProperty())
                .rating(req.getRating()).comment(req.getComment())
                .build();

        return mapToResponse(reviewRepository.save(review));
    }

    public PageResponse<ReviewResponse> getPropertyReviews(Long propertyId, int page, int size) {
        if (!propertyRepository.existsById(propertyId)) {
            throw new ResourceNotFoundException("Property", propertyId);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Review> result = reviewRepository.findByPropertyId(propertyId, pageable);
        return PageResponse.<ReviewResponse>builder()
                .content(result.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()))
                .page(result.getNumber()).size(result.getSize())
                .totalElements(result.getTotalElements()).totalPages(result.getTotalPages())
                .last(result.isLast()).build();
    }

    private ReviewResponse mapToResponse(Review r) {
        return ReviewResponse.builder()
                .id(r.getId()).propertyId(r.getProperty().getId())
                .reviewerId(r.getReviewer().getId())
                .reviewerName(r.getReviewer().getFullName())
                .reviewerAvatar(r.getReviewer().getAvatarUrl())
                .rating(r.getRating()).comment(r.getComment())
                .createdAt(r.getCreatedAt()).build();
    }
}
