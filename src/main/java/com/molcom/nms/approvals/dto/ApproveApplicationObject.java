package com.molcom.nms.approvals.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.molcom.nms.adminmanage.dto.AdminManagementModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproveApplicationObject {
    private String workFlowTrackingId;
    private String process;
    private String applicationId;
    private String workFlowStepNum;
    private String workFlowStepName;
    private String generateInvoice;
    private String assignedUserName;
    private String assignedUserEmail;
    private String assignedDate;
    private String approvalAction;
    private String authorizerUsername;
    private String authorizerEmail;
    private String comment;
    private List<ApprovalSupportingDocument> supportingDocument = new ArrayList<>();
    private AdminManagementModel detailOfAdmin = new AdminManagementModel();
}
