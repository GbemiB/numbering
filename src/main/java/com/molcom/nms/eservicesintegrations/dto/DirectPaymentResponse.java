package com.molcom.nms.eservicesintegrations.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectPaymentResponse {
    private String transactionRef;
    private String paymentLink;
}
