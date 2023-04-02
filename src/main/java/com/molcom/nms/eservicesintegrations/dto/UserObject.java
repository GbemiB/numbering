package com.molcom.nms.eservicesintegrations.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserObject {
    private String username;
    private String appUserEmail;
    private String mobileNumber;
    private String userType;
    private String dateCreated;
    private String firstName;
    private String lastName;
    private String middleName;
    private String image;
    private String appUserId;
    private String organizationId;
}
