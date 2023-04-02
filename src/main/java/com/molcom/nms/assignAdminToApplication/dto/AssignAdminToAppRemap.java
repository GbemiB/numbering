package com.molcom.nms.assignAdminToApplication.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssignAdminToAppRemap {
    private String process;
    private String applicationId;
    private String workFlowStepNum;
    private String workFlowStepName;
    private String generateInvoice;
    private String assignedUserName;
    private String assignedUserEmail;
    @Nullable
    private String assignedDate;
}
