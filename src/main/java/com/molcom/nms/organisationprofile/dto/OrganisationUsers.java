package com.molcom.nms.organisationprofile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganisationUsers {
    private String name;
    private String email;
}
