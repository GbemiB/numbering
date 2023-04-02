package com.molcom.nms.approvals.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproveApplicationModel {
    @ApiModelProperty(example = "GEOGRAPHICAL NUMBER ALLOCATION")
    private String process;
    @ApiModelProperty(example = "A000000142")
    private String applicationId;
    @ApiModelProperty(example = "2")
    private String workflowStepNum;
    @ApiModelProperty(example = "Evaluate")
    private String workFlowStepName;
    @ApiModelProperty(example = "APPROVED")
    private String approvalAction; // APPROVED, REJECTED
    @ApiModelProperty(example = "numbering_admin")
    private String authorizerUsername;
    @ApiModelProperty(example = "numberingadmin@yahoo.com")
    private String authorizerEmail;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @ApiModelProperty(example = "Test comment")
    private String comment;
    @ApiModelProperty(example = "FALSE")
    private String isMDA;
    private List<ApprovalSupportingDocument> supportingDocument = new ArrayList<>();
    @Nullable
    private List<String> rejectedNumbers;
    @Nullable
    private String replacementUrl;
}

