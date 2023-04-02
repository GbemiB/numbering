package com.molcom.nms.eservicesintegrations.dto.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeleteInvoice {
    @JsonProperty("ReceiptHandle")
    private String receiptHandle;
}
