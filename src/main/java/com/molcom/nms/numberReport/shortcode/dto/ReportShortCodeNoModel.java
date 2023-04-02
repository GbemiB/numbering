package com.molcom.nms.numberReport.shortcode.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportShortCodeNoModel {
    private String applicationId;
    private String numberType;
    private String shortCodeCategory;
    private String shortCodeService;
    private String shortCodeNumber; // Pick short code from this not min and max number
    private String minimumNumber;
    private String maximumNumber;
    private String createdDate;
    private String createdBy;
    private String allotee;
    private String quantity;
    private String purpose;
    private String category;
    private String services;
    private String bearerMedium;
    private String isNoSelected;
    private String allocationStatus;
    private String dateAllocated;
    private String allocationValidityFrom;
    private String allocationValidityTo;
    private String companyAllocatedTo;
    @Nullable
    private String createdShortCodeNumberId;

}


