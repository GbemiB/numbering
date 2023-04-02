package com.molcom.nms.numberReport.ispc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportIspcNoModel {
    private String applicationId;
    private String ispcNumber;
    private String createdDate;
    private String createdBy;
    private String quantity;
    private String purpose;
    private String allotee;
    private String isNoSelected;
    private String allocationStatus;
    private String dateAllocated;
    private String allocationValidityFrom;
    private String allocationValidityTo;
    private String companyAllocatedTo;
    @Nullable
    private String createdIspcNumberId;


}
