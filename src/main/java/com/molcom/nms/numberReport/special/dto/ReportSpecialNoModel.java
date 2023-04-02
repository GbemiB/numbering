package com.molcom.nms.numberReport.special.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportSpecialNoModel {
    private String applicationId;
    private String numberType;
    private String numberSubType;
    private String accessCode;
    private String minimumNumber;
    private String maximumNumber;
    private String createdDate;
    private String createdBy;
    private String allotee;
    private String quantity;
    private String purpose;
    private String bearerMedium;
    private String isNoSelected;
    private String allocationStatus;
    private String dateAllocated;
    private String allocationValidityFrom;
    private String allocationValidityTo;
    private String companyAllocatedTo;
    private String createdSpecialNumberId;
}


