package com.molcom.nms.fee.selectedFeeSchedule.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class SelectedFeeScheduleModel {
    @ApiModelProperty(example = "AP0001200")
    private String applicationId;
    @ApiModelProperty(example = "Short Code")
    private String feeName;
    @ApiModelProperty(example = "1000")
    private String feeAmount;
    @ApiModelProperty(example = "Short Code Services")
    private String feeDescription;
    @ApiModelProperty(example = "APPLICATION")
    private String invoiceType;
    @Nullable
    private String feeSelectedId;
}
