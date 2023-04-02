package com.molcom.nms.sendBulkEmail.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class EservicesSendMailRequest {
    @JsonIgnoreProperties("recipients")
    private List<String> recipients;

    @JsonIgnoreProperties("cc")
    private List<String> cc;

    @JsonIgnoreProperties("bcc")
    private List<String> bcc;

    @JsonIgnoreProperties("body")
    private String body;

    @JsonIgnoreProperties("body")
    private String subject;
}
