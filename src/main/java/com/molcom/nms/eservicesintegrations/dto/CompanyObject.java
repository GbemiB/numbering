package com.molcom.nms.eservicesintegrations.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyObject {
    private String companyName;
    private String phoneNumber;
    private String fax;
    private String website;
    private String headOfficeAddressCity;
    private String headOfficeAddressZip;
    private String headOfficeAddressPobox;
    private String headOfficeAddressPmb;
    private String companyContactPhone1;
    private String companyContactPhone2;
    private String companyContactAlternativeEmail;
    private String companyContactEmail;
    private String contractorId;
    private String nccId;
    private String headOfficeAddressStreet;
}
