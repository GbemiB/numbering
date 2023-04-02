package com.molcom.nms.allocationLetter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllocationLetterModel {
    private String id;
    private String allocationDate; // date of first allocation
    private String expiryDate; // expiration date
    private String numberType;
    private String numberSubType;
    private String selectedNumberValue;
    private String purpose;
    private String bearerMedium;
    private String numberingArea;
    private String areaCode;
    private String accessCode;
    private String subscriptionLine;
}
