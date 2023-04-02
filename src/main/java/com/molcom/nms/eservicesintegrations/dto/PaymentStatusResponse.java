package com.molcom.nms.eservicesintegrations.dto;

import lombok.Data;

@Data
public class PaymentStatusResponse {
    private String amount;
    private String status;
    private String paymentUrl;
    private String currencyCode;
    private String currencyShortName;
}
