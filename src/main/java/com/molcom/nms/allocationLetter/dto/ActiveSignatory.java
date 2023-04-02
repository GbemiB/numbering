package com.molcom.nms.allocationLetter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActiveSignatory {
    private String signatoryName;
    private String signatoryDesignation;
    private String signatorySignature;
    private String organisation;
}
