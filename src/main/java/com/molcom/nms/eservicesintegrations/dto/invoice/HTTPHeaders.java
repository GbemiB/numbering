package com.molcom.nms.eservicesintegrations.dto.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HTTPHeaders {
    @JsonProperty("x-amzn-requestid")
    private String xAmznRequestid;
    @JsonProperty("date")
    private String date;
    @JsonProperty("content-type")
    private String contentType;
    @JsonProperty("content-length")
    private String contentLength;
}
