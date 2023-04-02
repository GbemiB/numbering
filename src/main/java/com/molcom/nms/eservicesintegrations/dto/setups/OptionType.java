package com.molcom.nms.eservicesintegrations.dto.setups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionType {
    private String optionTypeId;
    private String dateCreated;
    private String createdBy;
    private String optionTypeName;
}
