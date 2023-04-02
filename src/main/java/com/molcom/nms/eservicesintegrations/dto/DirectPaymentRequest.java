package com.molcom.nms.eservicesintegrations.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectPaymentRequest {
    @ApiModelProperty(example = "AP0003003")
    private String applicationId;
    @ApiModelProperty(example = "Standard")
    private String numberType;
    @ApiModelProperty(example = "Geographical")
    private String numberSubType;
    @ApiModelProperty(example = "3000")
    private Integer amount;
    @ApiModelProperty(example = "d08e1fcc-1af0-4404-a26e-3e83e6410d99")
    private String revenueServiceId;
    @ApiModelProperty(example = "naira")
    private String currencyId;
    @ApiModelProperty(example = "Molcom")
    private String companyName;
    @ApiModelProperty(example = "Iseni Olu")
    private String payerName;
    @ApiModelProperty(example = "No 2 Abuja")
    private String payerAddress;
    @ApiModelProperty(example = "08034844220")
    private String payerPhone;
    @ApiModelProperty(example = "olu@gmail.com")
    private String payerEmail;
    @ApiModelProperty(example = "Payment for number creation")
    private String description;
    @ApiModelProperty(example = "3000")
    private String gifimisCode;
    @ApiModelProperty(example = "http://www.molcom.com")
    private String applicationUrl;
}
