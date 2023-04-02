package com.molcom.nms.number.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StandardNumberModel {
    @ApiModelProperty(example = "AP00001200")
    private String applicationId;
    @ApiModelProperty(example = "Geographical")
    private String subType;
    @ApiModelProperty(example = "Molcom Multi-concepts Ltd")
    private String companyName;
    @ApiModelProperty(example = "molcom@gmail.com")
    private String companyEmail;
    @ApiModelProperty(example = "09035104918")
    private String companyPhone;
    @ApiModelProperty(example = "151210")
    private String companyFax;
    @ApiModelProperty(example = "Abuja")
    private String companyState;
    @ApiModelProperty(example = "No 6, Abuja way")
    private String companyAddress;
    @ApiModelProperty(example = "molcom@gmail.com")
    private String correspondingEmail;
    @ApiModelProperty(example = "09023468927")
    private String correspondingPhone;
    @ApiModelProperty(example = "1214930")
    private String correspondingFax;
    @ApiModelProperty(example = "Abuja")
    private String correspondingState;
    @ApiModelProperty(example = "No 6, Abuja way")
    private String correspondingAddress;
    @ApiModelProperty(example = "YES") // YES or No values
    private String interconnectAgreement; // -- first save here
    @ApiModelProperty(example = "1")
    private String companyRepresentativeIdOne;
    @ApiModelProperty(example = "2")
    private String companyRepresentativeIdTwo;
    @ApiModelProperty(example = "78")
    private String coverageArea;
    @ApiModelProperty(example = "78")
    private String areaCode;
    @ApiModelProperty(example = "123")
    private String accessCode;
    @ApiModelProperty(example = "18394949 -173904044")
    private String selectedNumbers;
    @ApiModelProperty(example = "2")
    private String noOfTelcoCompanies;
    @ApiModelProperty(example = "Mtn, Glo, Airtel")
    private String telcoCompanies;
    @ApiModelProperty(example = "YES")
    private String haveYouReachedAgreement; // yes or no
    @ApiModelProperty(example = "YES")
    private String haveYouMetFinancialReq; // yes or no
    @ApiModelProperty(example = "YES")
    private String frequentAssignment; // yes or no
    @ApiModelProperty(example = "CABLE")
    private String radioOrCableDeployment; // yes or no // 2nd save here
    @ApiModelProperty(example = "Demo comment")
    private String undertakenComment; // -- 3rd save here
    @Nullable
    @ApiModelProperty(example = "New")
    private String applicationType;
    @Nullable
    @ApiModelProperty(example = "Pending")
    private String applicationStatus;
    @Nullable
    @ApiModelProperty(example = "Unpaid")
    private String applicationPaymentStatus; // awaiting payment if not paid
    @Nullable
    @ApiModelProperty(example = "Unpaid")
    private String allocationPaymentStatus;
    @ApiModelProperty(example = "Molcom")
    private String createdBy;
    @ApiModelProperty(example = "user@gmail.com")
    private String createdUserEmail;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @ApiModelProperty(example = "239.1.300")
    private String ipAddress;
    @ApiModelProperty(example = "1")
    private String quantity;
    @ApiModelProperty(example = "FALSE")
    private String isMDA;
    @ApiModelProperty(example = "FALSE")
    private String isApplicationAssigned;
    @Nullable
    private String currentStep;
    @Nullable
    private String isApprovalCompleted;
}
