package com.molcom.nms.number.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShortCodeObject {
    private String applicationId;
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String companyFax;
    private String companyState;
    private String companyAddress;
    private String correspondingEmail;
    private String correspondingPhone;
    private String correspondingFax;
    private String correspondingState;
    private String correspondingAddress;
    private String interconnectAgreement;
    private String companyRepresentativeIdOne;
    private String companyRepresentativeIdTwo;
    private String shortCodeCategory;
    private String shortCodeService;
    private String availableNumber;
    private String undertakenComment;
    private String applicationType;
    private String applicationStatus;
    private String applicationPaymentStatus;
    private String allocationPaymentStatus;
    private String createdBy;
    private String createdUserEmail;
    private String createdDate;
    private String ipAddress;
    private String quantity;
    private String currentStep;
    private String isApprovalCompleted;
    private String isMDA;
    private String isApplicationAssigned;
}
