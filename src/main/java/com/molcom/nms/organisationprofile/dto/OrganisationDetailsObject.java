package com.molcom.nms.organisationprofile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganisationDetailsObject {
    private List<OrganisationUsers> organisationUsers = new ArrayList<>();
    private List<OrganisationRepresentatives> organisationReps = new ArrayList<>();
    private List<OrganisationApplications> applications = new ArrayList<>();
    private List<OrganisationAllocatedNos> allocatedNos = new ArrayList<>();
}
