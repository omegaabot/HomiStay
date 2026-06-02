package com.homistay.service;

import com.homistay.dto.request.CreateSupportTicketRequest;
import com.homistay.dto.request.UpdateTicketStatusRequest;
import com.homistay.dto.response.PageResponse;
import com.homistay.dto.response.SupportDashboardResponse;
import com.homistay.dto.response.SupportTicketResponse;
import com.homistay.entity.SupportTicket;
import com.homistay.entity.User;
import com.homistay.exception.ResourceNotFoundException;
import com.homistay.repository.SupportTicketRepository;
import com.homistay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final UserRepository userRepository;

    @Transactional
    public SupportTicketResponse createTicket(Long userId, CreateSupportTicketRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        SupportTicket ticket = SupportTicket.builder()
                .user(user)
                .issueType(request.getIssueType())
                .issueDescription(request.getIssueDescription())
                .status("OPEN")
                .build();

        SupportTicket saved = supportTicketRepository.save(ticket);
        return toResponse(saved);
    }

    @Transactional
    public SupportTicketResponse assignTicket(Long ticketId, Long agentId) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("SupportTicket", ticketId));

        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("User", agentId));

        ticket.setAssignedTo(agent);
        ticket.setStatus("IN_PROGRESS");
        SupportTicket saved = supportTicketRepository.save(ticket);
        return toResponse(saved);
    }

    @Transactional
    public SupportTicketResponse updateTicketStatus(Long ticketId, UpdateTicketStatusRequest request) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("SupportTicket", ticketId));

        ticket.setStatus(request.getStatus().toUpperCase());
        SupportTicket saved = supportTicketRepository.save(ticket);
        return toResponse(saved);
    }

    public SupportTicketResponse getTicketById(Long ticketId) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("SupportTicket", ticketId));
        return toResponse(ticket);
    }

    public PageResponse<SupportTicketResponse> getUserTickets(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SupportTicket> ticketPage = supportTicketRepository.findByUserId(userId, pageable);
        return toPageResponse(ticketPage);
    }

    public PageResponse<SupportTicketResponse> getAssignedTickets(Long agentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SupportTicket> ticketPage = supportTicketRepository.findByAssignedToId(agentId, pageable);
        return toPageResponse(ticketPage);
    }

    public PageResponse<SupportTicketResponse> getTicketsByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SupportTicket> ticketPage = supportTicketRepository.findByStatus(status.toUpperCase(), pageable);
        return toPageResponse(ticketPage);
    }

    public PageResponse<SupportTicketResponse> getAllTickets(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SupportTicket> ticketPage = supportTicketRepository.findAll(pageable);
        return toPageResponse(ticketPage);
    }

    public SupportDashboardResponse getDashboardStats() {
        List<SupportTicket> all = supportTicketRepository.findAll();
        long open = all.stream().filter(t -> "OPEN".equals(t.getStatus())).count();
        long inProgress = all.stream().filter(t -> "IN_PROGRESS".equals(t.getStatus())).count();
        long resolved = all.stream().filter(t -> "RESOLVED".equals(t.getStatus())).count();
        long closed = all.stream().filter(t -> "CLOSED".equals(t.getStatus())).count();

        List<SupportTicketResponse> recent = supportTicketRepository.findTop10ByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());

        return SupportDashboardResponse.builder()
                .totalQueries(all.size())
                .openIssuesCount(open)
                .inProgressIssuesCount(inProgress)
                .resolvedIssuesCount(resolved)
                .closedIssuesCount(closed)
                .recentTickets(recent)
                .build();
    }

    private PageResponse<SupportTicketResponse> toPageResponse(Page<SupportTicket> page) {
        List<SupportTicketResponse> content = page.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return PageResponse.<SupportTicketResponse>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private SupportTicketResponse toResponse(SupportTicket ticket) {
        return SupportTicketResponse.builder()
                .id(ticket.getId())
                .userId(ticket.getUser().getId())
                .userEmail(ticket.getUser().getEmail())
                .userName(ticket.getUser().getFullName())
                .issueType(ticket.getIssueType())
                .issueDescription(ticket.getIssueDescription())
                .status(ticket.getStatus())
                .assignedToId(ticket.getAssignedTo() != null ? ticket.getAssignedTo().getId() : null)
                .assignedToName(ticket.getAssignedTo() != null ? ticket.getAssignedTo().getFullName() : null)
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}
