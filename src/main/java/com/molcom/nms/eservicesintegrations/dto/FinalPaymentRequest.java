package com.molcom.nms.eservicesintegrations.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinalPaymentRequest {
    private Integer amount;
    private String revenueServiceId;
    private String currencyId;
    private String companyName;
    private String payerName;
    private String payerAddress;
    private String payerPhone;
    private String payerEmail;
    private String description;
    private String gifimisCode;
    private String applicationUrl;
}
