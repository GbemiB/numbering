package com.molcom.nms.organisationprofile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganisationApplications {
    private String numberType;
    private String subType;
    private String applicationId;
    private String applicationStatus;
    private String quantity;
}
