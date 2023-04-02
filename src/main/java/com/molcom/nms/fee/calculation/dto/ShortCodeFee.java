package com.molcom.nms.fee.calculation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShortCodeFee {
    private String feename;
    private Integer amount;
    private String feeDescription;
}
