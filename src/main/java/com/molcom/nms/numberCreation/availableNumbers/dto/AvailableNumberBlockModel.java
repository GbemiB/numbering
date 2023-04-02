package com.molcom.nms.numberCreation.availableNumbers.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvailableNumberBlockModel {
    private String numberType;
    private String numberSubType;
    private String numberRange;
    private String numberBlock;
    private String numberBlockId;
}
