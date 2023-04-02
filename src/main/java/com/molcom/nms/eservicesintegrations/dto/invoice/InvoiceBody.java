package com.molcom.nms.eservicesintegrations.dto.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceBody {
    private String errorMessage;
    private String invoiceId;
    private String invoiceStatus;
}
