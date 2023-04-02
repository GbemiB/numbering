package com.molcom.nms.additionofservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdditionOfServiceModel {
    @ApiModelProperty(example = "A000000242")
    private String parentApplicationId;
    @ApiModelProperty(example = "AP000120")
    private String applicationId;
    @ApiModelProperty(example = "2017")
    private String shortCode;
    @ApiModelProperty(example = "4 Digits - Important Service")
    private String serviceCategory;
    @ApiModelProperty(example = "PENDING")
    private String applicationStatus;
    @ApiModelProperty(example = "Internet/ Data Service Provider - sms")
    private String currentPurpose;
    @ApiModelProperty(example = "TEST")
    private String additionOfServicePurpose;
    @ApiModelProperty(example = "TEST")
    private String additionOfServiceReason;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @ApiModelProperty(example = "Molcom")
    private String createdBy;
    @Nullable
    private String additionOfServiceId;
    @Nullable
    private String isApplicationAssigned;
    @Nullable
    private String isApprovalCompleted;

}
