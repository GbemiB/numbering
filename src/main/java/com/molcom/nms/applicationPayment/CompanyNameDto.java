package com.molcom.nms.applicationPayment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyNameDto {
    private String companyName;
}
