package com.molcom.nms.fee.calculation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeeCalculationRequest {
    private String applicationId;
    private Integer accessFee;
    private Integer lineFee;
    private Integer adminFee;
    private String numberType;
    private Integer numberSelected;
    private String numberSubType;
}
