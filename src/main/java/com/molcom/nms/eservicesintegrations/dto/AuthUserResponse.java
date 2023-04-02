package com.molcom.nms.eservicesintegrations.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.molcom.nms.security.Token;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthUserResponse {
    private UserObject userObject = new UserObject();
    private CompanyObject companyObject = new CompanyObject();
    private String isAdmin;
    private List<String> userRoles = new ArrayList<>();
    private Token jwt = new Token();
}
