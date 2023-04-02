package com.molcom.nms.eservicesintegrations.dto.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseMetadata {
    @JsonProperty("RequestId")
    private String requestId;
    @JsonProperty("HTTPStatusCode")
    private int httpStatusCode;
    @JsonProperty("HTTPHeaders")
    private HTTPHeaders httpHeaders = new HTTPHeaders();
}
