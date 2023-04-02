package com.molcom.nms.fee.calculation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FeeCalculationResponse {
    @ApiModelProperty(example = "4904000")
    private Integer allocationFee;
    @ApiModelProperty(example = "3004")
    private Integer renewalFee;
//    private Integer accessCodeFee;
//    private Integer adminFee;
//    private Integer lineFee;
}
