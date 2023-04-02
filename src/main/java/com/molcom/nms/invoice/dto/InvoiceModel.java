package com.molcom.nms.invoice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class InvoiceModel {
    @Nullable
    private String invoiceId;
    @ApiModelProperty(example = "Short code")
    private String numberType;
    @ApiModelProperty(example = "Short code")
    private String numberSubType;
    @ApiModelProperty(example = "AP0000123")
    private String applicationId;
    @ApiModelProperty(example = "Molcom")
    private String organization;
    @ApiModelProperty(example = "005-jfnfn-499595")
    private String transactionRefId;
    @Nullable
    private String invoiceNumber;
    @ApiModelProperty(example = "APPLICATION")
    private String invoiceType;
    @ApiModelProperty(example = "Unpaid")
    private String paymentStatus;
    @ApiModelProperty(example = "Molcom")
    private String initiatorUsername;
    @ApiModelProperty(example = "molcom@gmail.com")
    private String initiatorEmail;
    @ApiModelProperty(example = "200")
    private Integer amount;
    @ApiModelProperty(example = "Value Added Services")
    private String description;
    @ApiModelProperty(example = "")
    private String status;
    @ApiModelProperty(example = "2022-01-03")
    private String ValueDate;
    @Nullable
    private String shouldSendToEservices;
    @Nullable
    private String eservicesRequestId;


}

