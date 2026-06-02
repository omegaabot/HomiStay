package com.homistay.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class SupportDashboardResponse {
    private long totalQueries;
    private long openIssuesCount;
    private long inProgressIssuesCount;
    private long resolvedIssuesCount;
    private long closedIssuesCount;
    private List<SupportTicketResponse> recentTickets;
}
