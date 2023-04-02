package com.molcom.nms.organisationprofile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganisationRepresentatives {
    private String firstName;
    private String lastName;
    private String email;
    private String status;
    private String designation;
}
