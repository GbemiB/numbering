package com.molcom.nms.eservicesintegrations.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.molcom.nms.eservicesintegrations.dto.setups.CountryOfRegistration;
import com.molcom.nms.eservicesintegrations.dto.setups.HeadOfficeAddressState;
import com.molcom.nms.eservicesintegrations.dto.setups.OptionType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyDetailResponse {
    private String verificationStatus;
    private String tinNumber;
    private CountryOfRegistration countryOfRegistration = new CountryOfRegistration();
    private OptionType ownershipType = new OptionType();
    private OptionType companyType = new OptionType();
    private HeadOfficeAddressState headOfficeAddressState = new HeadOfficeAddressState();
    private String phoneNumber;
    private String fax;
    private String capital;
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
    private String rcNumber;
    private String nccId;
    private String dateOfRegistration;
    private String headOfficeAddressStreet;
    private String companyName;
}
