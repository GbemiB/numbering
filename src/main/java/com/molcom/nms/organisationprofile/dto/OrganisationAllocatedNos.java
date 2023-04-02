package com.molcom.nms.organisationprofile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganisationAllocatedNos {
    private String numberType;
    private String numberSubType;
    private String selectedNumberValue;
    private String quantity;
}
