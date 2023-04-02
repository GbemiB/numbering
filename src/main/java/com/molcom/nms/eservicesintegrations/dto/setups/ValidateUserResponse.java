package com.molcom.nms.eservicesintegrations.dto.setups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidateUserResponse {
    private String appUserId;
    private List<Applications> applications = new ArrayList<>();
    private String username;
    private String appUserEmail;
    private String mobileNumber;
    private String userType;
    private String dateCreated;
    private String firstName;
    private String lastName;
    private String middleName;
    private boolean active;
    private boolean emailVerified;
    private boolean phoneVerified;
    private String image;
    private Organization organization = new Organization();
    private Role role = new Role();
    private List<OrganizationUnits> organizationUnits = new ArrayList<>();
}
