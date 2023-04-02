package com.molcom.nms.eservicesintegrations.dto.setups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeadOfficeAddressState {
    private String stateId;
    private String stateName;
    private String capital;
    private String dateCreated;
    private String createdBy;
    private String dateModified;
    private String modifiedBy;
    private CountryCode countryCode = new CountryCode();
}
