package com.molcom.nms.eservicesintegrations.dto.setups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionObject {
    private String dateCreated;
    private String dateModified;
    private String modifiedBy;
    private String createdBy;
    private String optionId;
    private String optionName;
    private String optionDescription;
    private String actionAdditionalDetails;
    private OptionType optionType = new OptionType();
    private String parentOptionId;

}
