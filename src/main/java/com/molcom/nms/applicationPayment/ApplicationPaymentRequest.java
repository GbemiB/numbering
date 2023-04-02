package com.molcom.nms.applicationPayment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApplicationPaymentRequest {
    @ApiModelProperty(example = "A000000666")
    private String applicationId;
    @ApiModelProperty(example = "wyru-489i-ejjdj-49032939")
    private String transactionId;
    @ApiModelProperty(example = "Standard")
    private String numberType;
    @ApiModelProperty(example = "Geographical")
    private String numberSubType;
    @ApiModelProperty(example = "FALSE")
    private String isMDA;
}
