package com.molcom.nms.allocationLetter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationInfo {
    private String coverageArea;
    private String areaCode;
    private String accessCode;
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String companyState;
    private String companyFax;
}
