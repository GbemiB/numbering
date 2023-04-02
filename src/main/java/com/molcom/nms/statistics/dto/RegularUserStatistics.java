package com.molcom.nms.statistics.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RegularUserStatistics {
    // Counts for 5 recent applications across all number caegories
    List<RecentApplicationLog> recentApplicationLog = new ArrayList<>();
    // Counts across all number types
    private int submittedApplicationCount;
    private int unSubmittedApplicationCount;
    private int approvedApplicationCount;
    private int rejectedApplicationCount;
    // Counts for each number category
    private int totalStandardNumbersCount;
    private int totalShortCodesCount;
    private int totalISPCCount;
    private int totalSpecialNumbersCount;

}
