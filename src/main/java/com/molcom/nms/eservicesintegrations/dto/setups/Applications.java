package com.molcom.nms.eservicesintegrations.dto.setups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Applications {
    private int ordering;
    private String logoPath;
    private String applicationId;
    private String applicationName;
    private String applicationHost;
    private String publicAccessEnabled;
    private String applicationDescription;
    private String dateCreated;
}
