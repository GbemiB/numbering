package com.molcom.nms.fee.schedule.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeeScheduleModel {
    @ApiModelProperty(example = "Line Fee")
    private String feeType;
    @ApiModelProperty(example = "Standard")
    private String numberType;
    @ApiModelProperty(example = "Geographical")
    private String numberSubType;
    @ApiModelProperty(example = "Application")
    private String billingStage;
    @ApiModelProperty(example = "30")
    private String initialValue;
    @ApiModelProperty(example = "Fixed")
    private String initialValueType;
    @ApiModelProperty(example = "Y")
    private String isRenewable; // Y or N
    @ApiModelProperty(example = "30")
    private String renewableType;
    @ApiModelProperty(example = "Fixed")
    private String renewableValueType;
    @ApiModelProperty(example = "System")
    private String createdUser;
    @ApiModelProperty(example = "System")
    private String modifiedUser;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String modifiedDate;
    @Nullable
    private String feeScheduleId;

}
