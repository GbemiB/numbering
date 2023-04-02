package com.molcom.nms.billingcycle.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class BillingCycleModel {
    @ApiModelProperty(example = "Yearly")
    private String billingName;
    @ApiModelProperty(example = "Recurring")
    private String billingType;
    @ApiModelProperty(example = "1")
    private String billingPeriod;
    @ApiModelProperty(example = "year")
    private String billingCycle;
    @ApiModelProperty(example = "Molcom")
    private String createdUser;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @ApiModelProperty(example = "System")
    private String updatedBy;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String updatedDate;
    @Nullable
    private String billingId;


}
