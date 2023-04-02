package com.molcom.nms.allocationLetter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllocationLetterObject {
    private String allocationId;
    private String expiryDate;
    private String generatedDate;
    private String allocationDate; // date of first allocation
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String companyState;
    private String companyFax;
    private ActiveSignatory signatory = new ActiveSignatory();
    private List<AllocationLetterModel> numberInfo = new ArrayList<>();
}
