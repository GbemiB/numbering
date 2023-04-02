package com.molcom.nms.allocationLetter.service;

import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableCellGetRepository;
import com.molcom.nms.adminmanage.repository.AdminManagementRepo;
import com.molcom.nms.allocationLetter.dto.*;
import com.molcom.nms.allocationLetter.repository.AllocationLetterRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.sendBulkEmail.dto.EservicesSendMailRequest;
import com.molcom.nms.sendBulkEmail.service.IBulkEmailService;
import com.molcom.nms.signatory.dto.SignatoryModel;
import com.molcom.nms.signatory.repository.ISignatoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AllocationLetterService {

    @Autowired
    private AllocationLetterRepository repository;
    @Autowired
    private ISignatoryRepo signatoryRepo;
    @Autowired
    private IBulkEmailService bulkEmailService;
    @Autowired
    private AdminManagementRepo adminManagementRepo;
    @Autowired
    private GenericTableCellGetRepository genericTableCellGetRepository;

    /**
     * Service implementation to get details for allocation letter
     *
     * @param applicationId
     * @param numberType
     * @return
     * @throws Exception
     */
    public GenericResponse<AllocationLetterObject> getDetailsForAllocationLetter(String applicationId, String numberType) throws Exception {
        GenericResponse<AllocationLetterObject> genericResponse = new GenericResponse<>();
        AllocationLetterObject allocationLetterObject = new AllocationLetterObject();
        List<AllocationLetterModel> finalResultSet = new ArrayList<>();
        String companyNameVal = "";
        String numType = (numberType != null ? numberType.trim().toUpperCase() : "");

        // Get company info for the application
        ApplicationInfo companyInfo = repository.getApplicationInfo(numType, applicationId);

        try {
            List<AllocationLetterModel> resultSet = repository.getDetailsForAllocationLetter(applicationId);
            log.info("AutoFeeResponse code  {} ", resultSet);
            if (resultSet != null) {
                // get allocationDate and expiration date
                String allocatedDate = resultSet.get(0).getAllocationDate();
                String expirationDate = resultSet.get(0).getExpiryDate();

                // for each, get the coverage, area and access codes
                resultSet.forEach(val -> {
                    AllocationLetterModel allocation = new AllocationLetterModel();
                    ApplicationInfo applicationInfo = new ApplicationInfo();
                    try {
                        applicationInfo = repository.getApplicationInfo(val.getNumberType(), applicationId);
                        log.info("Application id  {}", applicationInfo);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    allocation.setAllocationDate(val.getAllocationDate());
                    allocation.setNumberType(val.getNumberType());
                    allocation.setNumberSubType(val.getNumberSubType());
                    allocation.setSelectedNumberValue(val.getSelectedNumberValue());
                    allocation.setId(val.getId());

                    // 1 million for national
                    if (val.getNumberSubType() != null && Objects.equals(val.getNumberSubType().toUpperCase(), "NATIONAL")) {
                        allocation.setSubscriptionLine("1000000");
                    }
                    // 10,000 for geographical
                    if (val.getNumberSubType() != null && Objects.equals(val.getNumberSubType().toUpperCase(), "GEOGRAPHICAL")) {
                        allocation.setSubscriptionLine("10000");
                    }
                    // 10,000 for special: vanity and toll-free
                    if (val.getNumberType() != null && Objects.equals(val.getNumberType().toUpperCase(), "SPECIAL")) {
                        allocation.setSubscriptionLine("10000");
                    }

                    allocation.setPurpose(val.getPurpose());
                    allocation.setBearerMedium(val.getBearerMedium());
                    if (applicationInfo != null) {
                        allocation.setAccessCode(applicationInfo.getAccessCode());
                        allocation.setAreaCode(applicationInfo.getAreaCode());
                        allocation.setNumberingArea(applicationInfo.getCoverageArea());
                    }
                    finalResultSet.add(allocation);
                });

                if (companyInfo != null) {
                    companyNameVal = companyInfo.getCompanyName();
                    log.info("Organisation name {}", companyNameVal);
                    // Add company details in for to response object
                    allocationLetterObject.setCompanyEmail(companyInfo.getCompanyEmail());
                    allocationLetterObject.setCompanyName(companyInfo.getCompanyName());
                    allocationLetterObject.setCompanyPhone(companyInfo.getCompanyPhone());
                    allocationLetterObject.setCompanyState(companyInfo.getCompanyState());
                    allocationLetterObject.setCompanyFax(companyInfo.getCompanyFax());
                }

                // Get current active signatory
                ActiveSignatory activeSignatory = new ActiveSignatory();
                // Get active signatory for the organisation with the application id
                SignatoryModel active = signatoryRepo.getActiveSignatory();
                if (active != null) {
                    activeSignatory.setSignatoryName(active.getSignatoryName());
                    activeSignatory.setSignatorySignature(active.getSignatorySignature());
                    activeSignatory.setSignatoryDesignation(active.getSignatoryDesignation());
                    activeSignatory.setOrganisation(active.getOrganisation());

                    // Add signatory info to response object
                    allocationLetterObject.setSignatory(activeSignatory);
                }

                allocationLetterObject.setAllocationDate(allocatedDate);
                allocationLetterObject.setAllocationId(applicationId);
                allocationLetterObject.setGeneratedDate(CurrentTimeStamp.getCurrentTimeStamp().toString());
                allocationLetterObject.setExpiryDate(expirationDate);

                // Add numbers allocated info to response object
                allocationLetterObject.setNumberInfo(finalResultSet);

                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(allocationLetterObject);
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    public GenericResponse<AllocationLetter> saveAllocationLetter(AllocationLetter model) throws Exception {
        GenericResponse<AllocationLetter> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            // Check if it's first time saving allocation letter
            int count = repository.allocationLetterCount(model.getApplicationId());

            if (count >= 1) {
                // Allocation letter has been saved before now
                responseCode = repository.saveAllocationLetter(model);
                log.info("AutoFeeResponse code  {} ", responseCode);
                if (responseCode == 1) {
//                    subseqAllocationLetterMail(model.getApplicationId());
                    genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                } else {
                    genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                }
            } else {
                // First time allocation
                responseCode = repository.saveAllocationLetter(model);
                log.info("AutoFeeResponse code  {} ", responseCode);
                if (responseCode == 1) {
                    firstAllocationLetterMail(model.getApplicationId());
                    genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                } else {
                    genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                }
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * Method to handle mailing service
     *
     * @param applicationId
     * @throws SQLException
     */
    @Async
    void firstAllocationLetterMail(String applicationId) throws SQLException {

        String link = "";

        AllocationLetter letter = repository.findByApplicationId(applicationId);
        if (letter != null) {
            link = letter.getAllocationLetterLink();

            EservicesSendMailRequest userMailRequest = new EservicesSendMailRequest();

            String numType = genericTableCellGetRepository.getNumberType(applicationId);
            String numSubType = genericTableCellGetRepository.getNumberSubType(applicationId);
            String companyNameVal = genericTableCellGetRepository.getCompanyNameOfApp(applicationId);
            String companyEmail = genericTableCellGetRepository.getCompanyEmail(applicationId);

            // send mail to user with allocation letter attached
            List<String> companyEmails = new ArrayList<>();
            assert companyEmails != null;
            companyEmails.add(companyEmail);
            userMailRequest.setRecipients(companyEmails); // should be email
            userMailRequest.setSubject("HELLO " + companyNameVal + ",");
            userMailRequest.setBody("<h3>Congratulations!, Your number application with application ID " + applicationId +
                    " is completed. See details below:  <h3/> " +
                    "  <br>" +
                    " Application ID: " + applicationId +
                    "  <br>" +
                    " Number Type: " + numType +
                    "  <br>" +
                    " Number Sub Type: " + numSubType +
                    "  <br>" +
                    " Kindly find attached link to allocation letter" +
                    "  <br>" +
                    "<a href= " + link + ">" + link + "</a> "
            );
            userMailRequest.setCc(null);
            userMailRequest.setBcc(null);

            try {
                Boolean isMailSentToUser = bulkEmailService.genericMailFunction(userMailRequest);
                log.info("Is Mail Sent To User {}", isMailSentToUser);

            } catch (Exception ex) {
                log.info("Exception occurred while sending mail {}", ex.getMessage());
            }
        }
    }


    /**
     * Method to handle mailing service
     * Note: Don't send mail for subsequent allocation view
     * Method not in use
     *
     * @param applicationId
     * @throws SQLException
     */
    @Async
    void subseqAllocationLetterMail(String applicationId) throws SQLException {
        String link = "";
        AllocationLetter letter = repository.findByApplicationId(applicationId);
        if (letter != null) {
            link = letter.getAllocationLetterLink();

            EservicesSendMailRequest userMailRequest = new EservicesSendMailRequest();

            String numType = genericTableCellGetRepository.getNumberType(applicationId);
            String numSubType = genericTableCellGetRepository.getNumberSubType(applicationId);
            String companyNameVal = genericTableCellGetRepository.getCompanyNameOfApp(applicationId);
            String companyEmail = genericTableCellGetRepository.getCompanyEmail(applicationId);

            // send mail to user with allocation letter attached
            List<String> companyEmails = new ArrayList<>();
            assert companyEmails != null;
            companyEmails.add(companyEmail);
            userMailRequest.setRecipients(companyEmails); // should be email
            userMailRequest.setSubject("HELLO " + companyNameVal + ",");
            userMailRequest.setBody("<h3>Your application details:  <h3/> " +
                    "  <br>" +
                    " Application ID: " + applicationId +
                    "  <br>" +
                    " Number Type: " + numType +
                    "  <br>" +
                    " Number Sub Type: " + numSubType +
                    "  <br>" +
                    " Kindly find attached link to allocation letter" +
                    "  <br>" +
                    "<a href= " + link + ">" + link + "</a> "
            );
            userMailRequest.setCc(null);
            userMailRequest.setBcc(null);
            try {
                Boolean isMailSentToUser = bulkEmailService.genericMailFunction(userMailRequest);
                log.info("Is Mail Sent To User {}", isMailSentToUser);

            } catch (Exception ex) {
                log.info("Exception occurred while sending mail {}", ex.getMessage());
            }
        }
    }

}
