package com.molcom.nms.assignAdminToApplication.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssignAdminToAppModel {
    @ApiModelProperty(example = "Standard number allocation Short code allocation")
    private String process;
    @ApiModelProperty(example = "AP000120")
    private String applicationId;
    @ApiModelProperty(example = "APPROVED")
    private String approvalAction;
    @ApiModelProperty(name = "1")
    private String workFlowStepNum;
    @ApiModelProperty(name = "Validation")
    private String workFlowStepName;
    @ApiModelProperty(name = "true")
    private String generateInvoice;
    private List<String> assignedUserName;
    private List<String> assignedUserEmail;
    @Nullable
    private String assignAdminToAppId;
}
