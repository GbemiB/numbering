package com.molcom.nms.invoice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CheckPaymentResponse {
    @ApiModelProperty(example = "200")
    private String amount;
    @ApiModelProperty(example = "Paid")
    private String status;
    @ApiModelProperty(example = "dummy")
    private String paymentUrl;
    @ApiModelProperty(example = "NGN")
    private String currencyCode;
    @ApiModelProperty(example = "NG")
    private String currencyShortName;
}
