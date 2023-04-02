package com.molcom.nms.fee.calculation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerateShortCodeInvoiceReq {
    List<ShortCodeFee> shortCodeFeeList = new ArrayList<>();
    private String applicationId;
}
