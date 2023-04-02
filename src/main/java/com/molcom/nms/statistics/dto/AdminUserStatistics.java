package com.molcom.nms.statistics.dto;

import lombok.Data;

@Data
public class AdminUserStatistics {
    private CoverageAreaCount coverageAreaCount = new CoverageAreaCount();
    private PendingApplicationCount pendingApplicationCount = new PendingApplicationCount();
    private ApprovedApplicationCount approvedApplicationCount = new ApprovedApplicationCount();
    private RejectedApplicationCount rejectedApplicationCount = new RejectedApplicationCount();
    private AdditionOfServicesCount additionOfServicesCount = new AdditionOfServicesCount();
}
