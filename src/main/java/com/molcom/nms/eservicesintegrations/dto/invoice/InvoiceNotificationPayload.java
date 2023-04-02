package com.molcom.nms.eservicesintegrations.dto.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceNotificationPayload {
    @JsonProperty("MessageId")
    public String messageId;
    @JsonProperty("ReceiptHandle")
    public String receiptHandle;
    @JsonProperty("MD5OfBody")
    public String mD5OfBody;
    @JsonProperty("Body")
    public String body;
}
