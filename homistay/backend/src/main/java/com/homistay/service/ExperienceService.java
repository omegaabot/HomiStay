package com.homistay.service;

import com.homistay.dto.request.BookingExperienceRequest;
import com.homistay.dto.request.ExperienceRequest;
import com.homistay.dto.response.ExperienceResponse;
import com.homistay.dto.response.PageResponse;
import com.homistay.entity.*;
import com.homistay.exception.BusinessException;
import com.homistay.exception.ResourceNotFoundException;
import com.homistay.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final BookingExperienceRepository bookingExperienceRepository;
    private final UserRepository userRepository;

    public PageResponse<ExperienceResponse> listAll(String city, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Experience> result = (city != null && !city.isBlank())
                ? experienceRepository.findByCityIgnoreCaseAndIsActiveTrue(city, pageable)
                : experienceRepository.findByIsActiveTrue(pageable);
        return toPageResponse(result);
    }

    public ExperienceResponse getById(Long id) {
        return mapToResponse(experienceRepository.findById(java.util.Objects.requireNonNull(id))
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id)));
    }

    @Transactional
    public ExperienceResponse create(ExperienceRequest req, String hostEmail) {
        User host = userRepository.findByEmail(hostEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Experience exp = Experience.builder()
                .host(host).title(req.getTitle()).description(req.getDescription())
                .pricePerPerson(req.getPricePerPerson()).durationHours(req.getDurationHours())
                .city(req.getCity()).maxParticipants(req.getMaxParticipants()).isActive(true)
                .build();
        return mapToResponse(experienceRepository.save(exp));
    }

    @Transactional
    public void bookExperience(BookingExperienceRequest req, String guestEmail) {
        Experience exp = experienceRepository.findById(java.util.Objects.requireNonNull(req.getExperienceId()))
                .orElseThrow(() -> new ResourceNotFoundException("Experience", req.getExperienceId()));
        if (!exp.getIsActive()) throw new BusinessException("Experience is not available");
        if (req.getParticipants() > exp.getMaxParticipants()) {
            throw new BusinessException("Exceeds max participants: " + exp.getMaxParticipants());
        }
        User guest = userRepository.findByEmail(guestEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        BigDecimal total = exp.getPricePerPerson().multiply(BigDecimal.valueOf(req.getParticipants()));
        bookingExperienceRepository.save(BookingExperience.builder()
                .guest(guest).experience(exp).bookingDate(req.getBookingDate())
                .participants(req.getParticipants()).totalPrice(total).status("CONFIRMED")
                .build());
    }

    private ExperienceResponse mapToResponse(Experience e) {
        return ExperienceResponse.builder()
                .id(e.getId()).hostId(e.getHost().getId()).hostName(e.getHost().getFullName())
                .title(e.getTitle()).description(e.getDescription())
                .pricePerPerson(e.getPricePerPerson()).durationHours(e.getDurationHours())
                .city(e.getCity()).maxParticipants(e.getMaxParticipants()).isActive(e.getIsActive())
                .build();
    }

    private PageResponse<ExperienceResponse> toPageResponse(Page<Experience> page) {
        return PageResponse.<ExperienceResponse>builder()
                .content(page.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()))
                .page(page.getNumber()).size(page.getSize())
                .totalElements(page.getTotalElements()).totalPages(page.getTotalPages())
                .last(page.isLast()).build();
    }
}
