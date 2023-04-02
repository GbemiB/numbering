package com.molcom.nms.eservicesintegrations.dto.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties
public class CreateInvoiceResponse {
    @JsonProperty("MD5OfMessageBody")
    private String md5OfMessageBody;
    @JsonProperty("MessageId")
    private String messageId;
    @JsonProperty("RetryAttempts")
    private String retryAttempts;
    @JsonProperty("ResponseMetadata")
    private ResponseMetadata responseMetadata = new ResponseMetadata();
    @Nullable
    @JsonProperty("message")
    private String message;
}
