package com.molcom.nms.numberReport.standard.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportStandardNoModel {
    private String applicationId;
    private String numberType;
    private String numberSubType;
    private String coverageArea;
    private String area;
    private String areaCode;
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
    @Nullable
    private String createdStandardNumberId;

}


