package com.molcom.nms.eservicesintegrations.dto.setups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationUnits {
    private String organizationUnitId;
    private String organizationId;
    private String unitName;
    private String unitDescription;
}
