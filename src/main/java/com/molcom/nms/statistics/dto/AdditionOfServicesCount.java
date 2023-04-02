package com.molcom.nms.statistics.dto;

import lombok.Data;

@Data
public class AdditionOfServicesCount {
    private int approvedServices;
    private int pendingServices;
    private int submittedServices;
    private int rejectedServices;
}
