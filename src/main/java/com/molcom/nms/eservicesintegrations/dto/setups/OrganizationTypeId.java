package com.molcom.nms.eservicesintegrations.dto.setups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationTypeId {
    private String organizationTypeId;
    private String organizationTypeName;
    private String organizationTypeDescription;
    private String dateCreated;
    private String createdBy;
}
