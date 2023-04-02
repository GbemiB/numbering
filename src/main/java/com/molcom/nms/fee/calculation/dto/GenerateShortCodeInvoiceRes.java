package com.molcom.nms.fee.calculation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerateShortCodeInvoiceRes {
    @ApiModelProperty(example = "1000000")
    private Integer allocationFee;
}
