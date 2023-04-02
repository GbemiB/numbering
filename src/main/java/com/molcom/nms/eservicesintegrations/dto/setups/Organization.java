package com.molcom.nms.eservicesintegrations.dto.setups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Organization {
    private String organizationId;
    private String dateCreated;
    private String organizationShortName;
    private String organizationDescription;
    private String organizationGroup;
    private String organizationLongName;
    private String logoPath;
    private OrganizationTypeId organizationTypeId = new OrganizationTypeId();

}
