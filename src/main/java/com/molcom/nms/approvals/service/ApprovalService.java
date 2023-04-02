package com.molcom.nms.approvals.service;

import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableCellGetRepository;
import com.molcom.nms.GenericDatabaseUpdates.repository.IGenericTableUpdateRepository;
import com.molcom.nms.adminmanage.dto.AdminManagementModel;
import com.molcom.nms.adminmanage.repository.IAdminManagementRepo;
import com.molcom.nms.approvals.dto.ApprovalSupportingDocument;
import com.molcom.nms.approvals.dto.ApproveApplicationModel;
import com.molcom.nms.approvals.dto.ApproveApplicationObject;
import com.molcom.nms.approvals.repository.ApprovalRepository;
import com.molcom.nms.assignAdminToApplication.dto.AssignAdminToAppModel;
import com.molcom.nms.fee.calculation.dto.FeeCalculationRequest;
import com.molcom.nms.fee.calculation.dto.FeeCalculationResponse;
import com.molcom.nms.fee.calculation.service.FeeCalculationNew;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.invoice.repository.InvoiceRepository;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
import com.molcom.nms.number.selectedNumber.repository.ISelectedNumbersRepo;
import com.molcom.nms.numberReport.ispc.dto.ReportIspcNoModel;
import com.molcom.nms.numberReport.ispc.repository.ReportIspcRepository;
import com.molcom.nms.numberReport.shortcode.repository.ReportShortCodeRepository;
import com.molcom.nms.numberReport.special.repository.ReportSpecialRepository;
import com.molcom.nms.numberReport.standard.repository.ReportStandardNoRepository;
import com.molcom.nms.sendBulkEmail.dto.EservicesSendMailRequest;
import com.molcom.nms.sendBulkEmail.service.IBulkEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ApprovalService {

    @Autowired
    private ApprovalRepository repository;
    @Autowired
    private IBulkEmailService bulkEmailService;
    @Autowired
    private IAdminManagementRepo adminManagementRepo;
    @Autowired
    private IGenericTableUpdateRepository genericTableUpdateRepository;
    @Autowired
    private GenericTableCellGetRepository genericTableCellGetRepository;
    @Autowired
    private FeeCalculationNew feeCalculationService;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private ISelectedNumbersRepo selectedNumbersRepo;
    @Autowired
    private ReportIspcRepository reportIspcRepository;
    @Autowired
    private ReportShortCodeRepository reportShortCodeRepository;
    @Autowired
    private ReportSpecialRepository reportSpecialRepository;
    @Autowired
    private ReportStandardNoRepository reportStandardNoRepository;


    /**
     * The purpose of this method is to approve a workflow process
     * and send email to the user approving the next step
     *
     * @param model
     * @return
     * @throws Exception
     */
    public GenericResponse<ApproveApplicationModel> save(ApproveApplicationModel model) throws Exception {
        GenericResponse<ApproveApplicationModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            int count = repository.checkIfAlreadyApproved(model);
            log.info("Count {}", count);
            if (count >= 1) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage("Application step has already been actioned on");
            } else {
                if (model.getApprovalAction().equalsIgnoreCase("REJECTED")) {
                    // IF approval step is REJECTED

                    // reject application step
                    responseCode = repository.approveApplicationStep(model);
                    log.info("AutoFeeResponse code for reject of approval step ::: {} ", responseCode);

                    // Get rejected numbers for the application
                    List<SelectedNumberModel> rejectedNumbers = selectedNumbersRepo.getRejectedNumbers(model.getApplicationId());

                    // If reject numbers and replacement url is available, it means it in approval step of number rejection
                    if (rejectedNumbers != null && model.getRejectedNumbers() != null) {
                        sendMailForRejectionStep(model.getApplicationId(), rejectedNumbers, model.getReplacementUrl(), model.getAuthorizerEmail());
                    }

                } else {
                    // IF approval step is APPROVED
                    // approve application step
                    responseCode = repository.approveApplicationStep(model);
                    log.info("AutoFeeResponse code for approve of approval step:::: {} ", responseCode);

                    if (responseCode != 0) {
                        // send mail to next approval admin
                        try {
                            boolean isMailSent = sendMailToNextApprovalAdminAndUser(model);
                            log.info("Is Mail sent {} ", isMailSent);
                        } catch (Exception e) {
                            log.info("Exception occurred while sending mail {}", e.getMessage());
                        }

                        // update approval completed status if all process is completed
                        updateApprovalCompletedStatus(model.getProcess(), model.getApplicationId());

                        // check if next approve step is invoice step
                        // If it's invoice step, generate invoice since the call is successful here
                        String nextStep = repository.getNextStep(model.getWorkflowStepNum());

                        List<AssignAdminToAppModel> nexttWorkFlow = repository.getNextApprovalProcess(
                                model.getApplicationId(),
                                model.getProcess(),
                                nextStep);
                        if (nexttWorkFlow != null && nexttWorkFlow.size() >= 1) {
                            String isInvoiceStage = nexttWorkFlow.get(0).getGenerateInvoice();
                            log.info("Is step invoice stage {}", isInvoiceStage);

                            // if the approved step is invoice step...call invoice to determine to generate invoice
                            // based on number type of the application been approved
                            if (isInvoiceStage != null && Objects.equals(isInvoiceStage.toUpperCase(), "TRUE")) {

                                // if MDA generate invoice with 0 amount as allocation fee and status as paid
                                // also set to `ShouldSendToEservices` as 'FALSE' and `IsInvoiceResolvedByEservices`
                                // as 'TRUE'
                                if (model.getIsMDA() != null &&
                                        Objects.equals(model.getIsMDA().toUpperCase(), "TRUE")) {
                                    String num = genericTableCellGetRepository.getNumberType(model.getApplicationId());
                                    String subNum = genericTableCellGetRepository.getNumberSubType(model.getApplicationId());
                                    String compName = genericTableCellGetRepository.getCompanyNameOfApp(model.getApplicationId());
                                    if (num != null && subNum != null && compName != null) {
                                        invoiceRepository.persistZeroFeeInvoiceForMDA(num, subNum, model.getApplicationId(),
                                                compName, model.getAuthorizerUsername(), model.getAuthorizerEmail(),
                                                subNum + " NUMBER ALLOCATION FEE");

                                        // update allocation payment to paid
                                        log.info("Number sub type {} ", subNum);
                                        boolean updateAllocationPayment = updateAllocationPayment(model.getApplicationId(), subNum);
                                        log.info("Allocation status updated for application id {} status is:  {} ",
                                                model.getApplicationId(), updateAllocationPayment);

                                        // update selected number allocation expiration duration
                                        int updatedSelectedNumber = selectedNumbersRepo.updateAfterAllocation
                                                (model.getApplicationId(), compName);
                                        log.info("Selected number allocation status updated for application id {} is : {} ",
                                                model.getApplicationId(), updatedSelectedNumber);

                                    }
                                } else {
                                    String res = callForInvoiceGeneration(model.getApplicationId());
                                    log.info("AutoFeeResponse from call to determine to generate invoice {} ", res);
                                }
                            }
                        }

                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                    } else {
                        genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                    }
                }
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!! ", exception.getStackTrace());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Call for invoice generation
     * It determines the number type based on the application id
     * if it's standard or special number, auto generation of invoice begins
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    private String callForInvoiceGeneration(String applicationId) throws Exception {
        String responseMessage = "";
        String numType = genericTableCellGetRepository.getNumberType(applicationId);
        String numSubType = genericTableCellGetRepository.getNumberSubType(applicationId);
        log.info("Number type {} and number subtype are  {} : ", numType, numSubType);
        if (numType != null && numSubType != null) {
            // Auto generate allocation invoice for standard and special number
            FeeCalculationRequest feeCalculationRequest = new FeeCalculationRequest();
            feeCalculationRequest.setApplicationId(applicationId);
            feeCalculationRequest.setNumberSubType(numSubType);
            feeCalculationRequest.setNumberType(numType);

            // If direct payment is made, check if number type is standard or special number and call for fee calculation
            // Inside the fee calculation service, when the allocation fee is calculated, it is persisted in invoice table
            // which is what make up auto generate of invoice for standard and special number
            // short code invoice is manually generated and no allocation fee for ISPC

            if (Objects.equals(numType, "STANDARD") ||
                    Objects.equals(numType, "SPECIAL")) {
                GenericResponse<FeeCalculationResponse> resp = feeCalculationService.feeCalculator(feeCalculationRequest);
                log.info("Send application for allocation fee calculation where invoice will be generates {}", resp);

                if (resp != null) {
                    responseMessage = "Invoice successfully generated for " + numSubType + " number";
                } else {
                    responseMessage = "Invoice not successfully generated for " + numSubType + " number";
                }

            } else {
                responseMessage = "Number subtype " + numSubType + " number" + "does not auto generate invoice";
            }
        } else {
            responseMessage = "Number subtype retrieval failed ";
        }
        return responseMessage;
    }

    /**
     * Method to update approval completed status
     *
     * @param process
     * @param applicationId
     * @throws Exception
     */
    private void updateApprovalCompletedStatus(String process, String applicationId) throws Exception {
        log.info("Process and application are {} {}", process, applicationId);
        String pro = (process != null ? process.trim().toUpperCase() : "");
        int updateIsApprovalCompleted = 0;
        int updateApplicationStatus = 0;

        // check if approval is completed
        boolean isApprovalCompleted = repository.checkIfApprovalIsCompleted(applicationId);
        log.info("Is approval completed {} ", isApprovalCompleted);
        log.info("Process {} ", pro);
        try {

            if (isApprovalCompleted) {
                if (pro.contains("NATIONAL") || pro.contains("GEOGRAPHICAL")) {
                    // Update IsApprovalCompleted column to true
                    updateIsApprovalCompleted = genericTableUpdateRepository.updateTableColumn("StandardNumber",
                            "IsApprovalCompleted",
                            "TRUE",
                            "ApplicationId",
                            applicationId);
                    log.info("Update is approval completed {}", updateIsApprovalCompleted);

                    // Update ApplicationStatus column to Approved
                    updateApplicationStatus = genericTableUpdateRepository.updateTableColumn("StandardNumber",
                            "ApplicationStatus",
                            "APPROVED",
                            "ApplicationId",
                            applicationId);
                    log.info("Update application status updated to Approved {}", updateApplicationStatus);

                    int updateToAllocated = reportStandardNoRepository.updateAfterAllocation(applicationId);
                    log.info("Application is updated to allocated in number report table {} ", updateToAllocated);

                } else if (pro.contains("VANITY") || pro.contains("TOLL-FREE") || pro.contains("TOLLFREE")) {
                    // Update IsApprovalCompleted column to true
                    updateIsApprovalCompleted = genericTableUpdateRepository.updateTableColumn("SpecialNumbers",
                            "IsApprovalCompleted",
                            "TRUE",
                            "ApplicationId",
                            applicationId);
                    log.info("Update is approval completed {}", updateIsApprovalCompleted);

                    // Update ApplicationStatus column to Approved
                    updateApplicationStatus = genericTableUpdateRepository.updateTableColumn("SpecialNumbers",
                            "ApplicationStatus",
                            "APPROVED",
                            "ApplicationId",
                            applicationId);
                    log.info("Update application status updated to Approved {}", updateApplicationStatus);

                    int updateToAllocated = reportSpecialRepository.updateAfterAllocation(applicationId);
                    log.info("Application is updated to allocated in number report table {} ", updateToAllocated);

                } else if (pro.contains("ISPC")) {
                    // Update IsApprovalCompleted column to true
                    updateIsApprovalCompleted = genericTableUpdateRepository.updateTableColumn("IspcNumbers",
                            "IsApprovalCompleted",
                            "TRUE",
                            "ApplicationId",
                            applicationId);
                    log.info("Update is approval completed {}", updateIsApprovalCompleted);

                    // Update ApplicationStatus column to Approved
                    updateApplicationStatus = genericTableUpdateRepository.updateTableColumn("IspcNumbers",
                            "ApplicationStatus",
                            "APPROVED",
                            "ApplicationId",
                            applicationId);
                    log.info("Update application status updated to Approved {}", updateApplicationStatus);

                    // Since no allocation payment or invoice for ISPC. Send for ISPC number report
                    log.info("Ready to send ISPC for reporting ");
                    ReportIspcNoModel reportIspcNoModel = new ReportIspcNoModel();
                    String quantity = genericTableCellGetRepository.getApplicationQuantity(applicationId);
                    String companyNameVal = genericTableCellGetRepository.getCompanyNameOfApp(applicationId);
                    Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
                    Timestamp nextOneYear = CurrentTimeStamp.getNextYearTimeStamp(todayDate, 365);
                    List<SelectedNumberModel> selectedNumberModels = selectedNumbersRepo.getSelectedNumber(applicationId);
                    log.info("Selected numbers for ISPC {} ", selectedNumberModels);
                    if (selectedNumberModels != null) {
                        selectedNumberModels.forEach(no -> {
                            reportIspcNoModel.setApplicationId(no.getApplicationId());
                            reportIspcNoModel.setIspcNumber(no.getSelectedNumberValue());
                            reportIspcNoModel.setCreatedBy("SYSTEM");
                            reportIspcNoModel.setAllotee("");
                            reportIspcNoModel.setPurpose(no.getPurpose());
                            reportIspcNoModel.setAllocationValidityFrom(todayDate.toString());
                            reportIspcNoModel.setAllocationValidityTo(nextOneYear.toString());
                            reportIspcNoModel.setDateAllocated(CurrentTimeStamp.getCurrentTimeStamp().toString());
                            reportIspcNoModel.setCompanyAllocatedTo(companyNameVal);
                            // Status is updated to allocated bacause no allocation fee
                            reportIspcNoModel.setAllocationStatus("ALLOCATED");
                            reportIspcNoModel.setQuantity(quantity);

                            log.info("Payload for ISPC Number Report {}", reportIspcNoModel);
                            try {
                                int isReportedIspc = reportIspcRepository.internalReport(reportIspcNoModel);
                                log.info("Number reporting status for application if {} status {} ", no.getApplicationId(), isReportedIspc);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                } else if (pro.contains("SHORT CODE ALLOCATION")
                        || pro.contains("SHORT CODE RENEWAL")) {
                    // Update IsApprovalCompleted column to true
                    updateIsApprovalCompleted = genericTableUpdateRepository.updateTableColumn("ShortCodeNumbers",
                            "IsApprovalCompleted",
                            "TRUE",
                            "ApplicationId",
                            applicationId);
                    log.info("Update is approval completed {}", updateIsApprovalCompleted);

                    // Update ApplicationStatus column to Approved
                    updateApplicationStatus = genericTableUpdateRepository.updateTableColumn("ShortCodeNumbers",
                            "ApplicationStatus",
                            "APPROVED",
                            "ApplicationId",
                            applicationId);
                    log.info("Update application status updated to Approved {}", updateApplicationStatus);

                    int updateToAllocated = reportShortCodeRepository.updateAfterAllocation(applicationId);
                    log.info("Application is updated to allocated in number report table {} ", updateToAllocated);

                } else if (pro.contains("ADDITION OF SHORT CODE SERVICE") || pro.contains("ADDITION")) {

                    // Update IsApprovalCompleted column to true
                    updateIsApprovalCompleted = genericTableUpdateRepository.updateTableColumn("AdditionOfService",
                            "IsApprovalCompleted",
                            "TRUE",
                            "ApplicationId",
                            applicationId);
                    log.info("Update is approval completed for addition of services {}", updateIsApprovalCompleted);


                    // Update ApplicationStatus column to Approved
                    updateApplicationStatus = genericTableUpdateRepository.updateTableColumn("AdditionOfService",
                            "ApplicationStatus",
                            "APPROVED",
                            "ApplicationId",
                            applicationId);
                    log.info("Update application status for addition of service updated to Approved {}", updateApplicationStatus);

                } else {
                    log.info("Application is not found");
                }

                // Send mail to user and admin that application process is completed
                boolean isMailSent = sendMailThatAppIsCompToAdminAndUser(applicationId);
                log.info("Mail sent for completed application {}", isMailSent);
            }
        } catch (Exception exe) {
            log.info("Exception occurred during approval {} ", exe.getMessage());
        }
    }

    /**
     * Mail section
     * This method send email to the user approving the next workflow step
     * This method is called after a successful approval of a workflow step
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Async
    boolean sendMailToNextApprovalAdminAndUser(ApproveApplicationModel model) throws SQLException {
        // Get next approval step
        String nextStepNo = repository.getNextStep(model.getWorkflowStepNum());
        String nextStepVal = (nextStepNo != null ? nextStepNo : "");

        // Get next approval flow
        List<AssignAdminToAppModel> approveApplicationModelList = repository.getNextApprovalProcess(
                model.getApplicationId(),
                model.getProcess(),
                nextStepVal);

        if (approveApplicationModelList.size() >= 1) {
            AssignAdminToAppModel nextApprovalFlow = approveApplicationModelList.get(0);

            // Get emails of users for the next approval step
            if (nextApprovalFlow != null && nextApprovalFlow.getAssignedUserEmail() != null) {

                // If approval process is invoice, send mail to user and admin
                if (Objects.equals(nextApprovalFlow.getGenerateInvoice().toUpperCase(), "TRUE")) {
                    EservicesSendMailRequest adminMailRequest = new EservicesSendMailRequest();
                    EservicesSendMailRequest userMailRequest = new EservicesSendMailRequest();

                    String appId = nextApprovalFlow.getApplicationId();
                    String numType = genericTableCellGetRepository.getNumberType(appId);
                    String numSubType = genericTableCellGetRepository.getNumberSubType(appId);
                    String companyNameVal = genericTableCellGetRepository.getCompanyNameOfApp(appId);
                    String companyEmail = genericTableCellGetRepository.getCompanyEmail(appId);

                    // Send mail to next admin
                    List<String> adminEmailsVal = nextApprovalFlow.getAssignedUserEmail();
                    adminMailRequest.setRecipients(adminEmailsVal); // should be email
                    adminMailRequest.setSubject("APPLICATION APPROVAL PROCESS");
                    adminMailRequest.setBody("<h3>An application require your attention. See details below: <h3/> " +
                            "  <br>" +
                            " Application ID: " + appId +
                            "  <br>" +
                            " Number Type: " + numType +
                            "  <br>" +
                            " Number Sub Type: " + numSubType +
                            "  <br>" +
                            " login to take action");
                    adminMailRequest.setCc(null);
                    adminMailRequest.setBcc(null);

                    // send mail to user
                    List<String> companyEmails = new ArrayList<>();
                    assert companyEmails != null;
                    companyEmails.add(companyEmail);
                    userMailRequest.setRecipients(companyEmails); // should be email
                    userMailRequest.setSubject("HELLO " + companyNameVal + ",");
                    userMailRequest.setBody("<h3>Congratulations!, Your number application with application ID " + appId +
                            " has been reviewed successfully and has moved to the next stage ("
                            + nextApprovalFlow.getWorkFlowStepName() + "). " +
                            "The step require invoice action. Please login to take action  <h3/> ");
                    userMailRequest.setCc(null);
                    userMailRequest.setBcc(null);

                    try {
                        Boolean isMailSentToAdmin = bulkEmailService.genericMailFunction(adminMailRequest);
                        Boolean isMailSentToUser = bulkEmailService.genericMailFunction(userMailRequest);
                        log.info("Is Mail Sent To Admin {}", isMailSentToAdmin);
                        log.info("Is Mail Sent To User{}", isMailSentToUser);
                    } catch (Exception ex) {
                        log.info("Exception occurred while sending mail {}", ex.getMessage());
                    }
                } else {
                    EservicesSendMailRequest adminMailRequest = new EservicesSendMailRequest();
                    EservicesSendMailRequest userMailRequest = new EservicesSendMailRequest();

                    String appId = nextApprovalFlow.getApplicationId();
                    String numType = genericTableCellGetRepository.getNumberType(appId);
                    String numSubType = genericTableCellGetRepository.getNumberSubType(appId);
                    String companyNameVal = genericTableCellGetRepository.getCompanyNameOfApp(appId);
                    String companyEmail = genericTableCellGetRepository.getCompanyEmail(appId);

                    // Send mail to next admin
                    List<String> adminEmailsVal = nextApprovalFlow.getAssignedUserEmail();
                    adminMailRequest.setRecipients(adminEmailsVal); // should be email
                    adminMailRequest.setSubject("APPLICATION APPROVAL PROCESS");
                    adminMailRequest.setBody("<h3>An application require your attention. See details below: <h3/> " +
                            "  <br>" +
                            " Application ID: " + appId +
                            "  <br>" +
                            " Number Type: " + numType +
                            "  <br>" +
                            " Number Sub Type: " + numSubType +
                            "  <br>" +
                            " login to take action");
                    adminMailRequest.setCc(null);
                    adminMailRequest.setBcc(null);

                    // send mail to user
                    List<String> companyEmails = new ArrayList<>();
                    assert companyEmails != null;
                    companyEmails.add(companyEmail);
                    userMailRequest.setRecipients(companyEmails);
                    userMailRequest.setSubject("HELLO " + companyNameVal + ",");
                    userMailRequest.setBody("<h3>Congratulations!, Your number application with application ID " + appId +
                            " has been reviewed successfully and has moved to the next stage ("
                            + nextApprovalFlow.getWorkFlowStepName() + "). <h3/> ");
                    userMailRequest.setCc(null);
                    userMailRequest.setBcc(null);

                    try {
                        Boolean isMailSentToAdmin = bulkEmailService.genericMailFunction(adminMailRequest);
                        Boolean isMailSentToUser = bulkEmailService.genericMailFunction(userMailRequest);
                        log.info("Is Mail Sent To Admin {}", isMailSentToAdmin);
                        log.info("Is Mail Sent To User{}", isMailSentToUser);

                        return isMailSentToAdmin & isMailSentToAdmin;
                    } catch (Exception ex) {
                        log.info("Exception occurred while sending mail {}", ex.getMessage());
                    }
                }
            }
        }
        return false;
    }


    /**
     * Mail section
     * This method send email to the user approving the next workflow step
     * This method is called after a successful approval of a workflow step
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Async
    boolean sendMailThatAppIsCompToAdminAndUser(String applicationId) throws SQLException {
        EservicesSendMailRequest adminMailRequest = new EservicesSendMailRequest();
        EservicesSendMailRequest userMailRequest = new EservicesSendMailRequest();

        String numType = genericTableCellGetRepository.getNumberType(applicationId);
        String numSubType = genericTableCellGetRepository.getNumberSubType(applicationId);
        String companyNameVal = genericTableCellGetRepository.getCompanyNameOfApp(applicationId);
        String companyEmail = genericTableCellGetRepository.getCompanyEmail(applicationId);

        // Send mail to next admin
        List<String> adminEmailsVal = adminManagementRepo.getCollaboratorAdmins();
        adminMailRequest.setRecipients(adminEmailsVal); // should be email
        adminMailRequest.setSubject("APPLICATION APPROVAL PROCESS");
        adminMailRequest.setBody("<h3>Application approval process is completed for application Id "
                + applicationId + ". See details below: <h3/> " +
                "  <br>" +
                " Application ID: " + applicationId +
                "  <br>" +
                " Number Type: " + numType +
                "  <br>" +
                " Number Sub Type: " + numSubType +
                "  <br>" +
                " login to take action");
        adminMailRequest.setCc(null);
        adminMailRequest.setBcc(null);

        // send mail to user
        List<String> companyEmails = new ArrayList<>();
        assert companyEmails != null;
        companyEmails.add(companyEmail);
        userMailRequest.setRecipients(companyEmails);
        userMailRequest.setSubject("HELLO " + companyNameVal + ",");
        userMailRequest.setBody("<h3>Congratulations!, Approval for your number application with application ID " + applicationId +
                " is completed. <h3/> ");
        userMailRequest.setCc(null);
        userMailRequest.setBcc(null);

        try {
            Boolean isMailSentToAdmin = bulkEmailService.genericMailFunction(adminMailRequest);
            Boolean isMailSentToUser = bulkEmailService.genericMailFunction(userMailRequest);
            log.info("Is Mail Sent To Admin {}", isMailSentToAdmin);
            log.info("Is Mail Sent To User{}", isMailSentToUser);
        } catch (Exception ex) {
            log.info("Exception occurred while sending mail {}", ex.getMessage());
        }
        return false;
    }


    /**
     * Mail section
     * This method send email to company to inform about numbers to be dropped
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Async
    boolean sendMailForRejectionStep(String applicationId, List<SelectedNumberModel> nos, String replaceUrl, String authorizerEmail) throws SQLException {
        List<String> numberList = new ArrayList<>();
        String numberString = "";
        String replacementUrl = "";
        List<String> adminEmails = new ArrayList<>();

        nos.forEach(no -> {
            numberList.add(no.getSelectedNumberValue());
        });

        // Convert array of string to comma seperated string
        numberString = String.join(",", numberList);
        log.info("Number  string{} ", numberString);

        // Send mail to user about rejected number
        EservicesSendMailRequest adminMailRequest = new EservicesSendMailRequest();
        EservicesSendMailRequest userMailRequest = new EservicesSendMailRequest();

        String numType = genericTableCellGetRepository.getNumberType(applicationId);
        String numSubType = genericTableCellGetRepository.getNumberSubType(applicationId);
        String companyName = genericTableCellGetRepository.getCompanyNameOfApp(applicationId);
        String companyEmail = genericTableCellGetRepository.getCompanyEmail(applicationId);
        replacementUrl = replaceUrl;

        // Send mail to admin that rejected the number
        adminEmails.add(authorizerEmail);
        adminMailRequest.setRecipients(adminEmails); // send to admin that rejected the number
        adminMailRequest.setSubject("APPLICATION APPROVAL PROCESS");
        adminMailRequest.setBody("<h3>The following numbers: " + numberString + " were rejected for application ID " + applicationId +
                "." +
                "  <br>" +
                " See details of below: <h3/> " +
                "  <br>" +
                " Application ID: " + applicationId +
                "  <br>" +
                " Number Type: " + numType +
                "  <br>" +
                " Number Sub Type: " + numSubType +
                "  <br>" +
                " The user will be instructed to login to replace or drop these numbers.");
        adminMailRequest.setCc(null);
        adminMailRequest.setBcc(null);

        // send mail to user
        List<String> companyEmails = new ArrayList<>();
        companyEmails.add(companyEmail);
        userMailRequest.setRecipients(companyEmails); // should be email
        assert companyEmails != null;
        userMailRequest.setSubject("HELLO " + companyName + ",");
        userMailRequest.setBody("<h3> The following numbers: " + numberString + " were rejected for your application " +
                "with application id " + applicationId + " Please replace numbers using this link. <a href= " + replacementUrl + ">" + replacementUrl + "</a> " +
                "." +
                "  <br>" +
                " See details of application below:  <h3/> " +
                "  <br>" +
                " Application ID: " + applicationId +
                "  <br>" +
                " Number Type: " + numType +
                "  <br>" +
                " Number Sub Type: " + numSubType +
                "  <br>" +
                "<h3/>  ");
        userMailRequest.setCc(null);
        userMailRequest.setBcc(null);

        try {
            Boolean isMailSentToAdmin = bulkEmailService.genericMailFunction(adminMailRequest);
            Boolean isMailSentToUser = bulkEmailService.genericMailFunction(userMailRequest);
            log.info("Is Mail Sent To Admin {}", isMailSentToAdmin);
            log.info("Is Mail Sent To User{}", isMailSentToUser);

            return isMailSentToAdmin & isMailSentToUser;
        } catch (Exception ex) {
            log.info("Exception occurred while sending mail {}", ex.getMessage());
        }
        return false;
    }

    /**
     * This method get the approval record for an application
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    public GenericResponse<List<ApproveApplicationObject>> getApprovalRecord(String applicationId) throws Exception {
        GenericResponse<List<ApproveApplicationObject>> genericResponse = new GenericResponse<>();
        List<ApproveApplicationObject> finalResultSet = new ArrayList<>();

        try {
            List<ApproveApplicationObject> resultSet = repository.getApplicationApprovalProcess(applicationId);
            log.info("ResultSet {} ", resultSet);
            if (resultSet != null) {

                resultSet.forEach(result -> {
                    try {
                        // Get the attached supporting document for the step
                        List<ApprovalSupportingDocument> documentList = repository.getApplicationSupportingDoc(result.getApplicationId(),
                                result.getWorkFlowStepNum());

                        // Get the full details of the authorised username for the step
                        List<AdminManagementModel> adminList = adminManagementRepo.getAdminByUsername(result.getAuthorizerUsername());
                        AdminManagementModel admin = new AdminManagementModel();
                        // If admin details is available, map in object
                        if (adminList != null && adminList.size() >= 1) {
                            log.info("two ");
                            AdminManagementModel detailOfAdmin = adminList.get(0);

                            admin.setOrganisation(detailOfAdmin.getOrganisation());
                            admin.setSignature(detailOfAdmin.getSignature());
                            admin.setAssignedRole(detailOfAdmin.getAssignedRole());
                            admin.setName(detailOfAdmin.getName());
                            admin.setEmail(detailOfAdmin.getEmail());
                            admin.setUserName(detailOfAdmin.getUserName());
                            log.info("Admin {}", admin);

                        }
                        result.setDetailOfAdmin(admin);
                        result.setSupportingDocument(documentList);

                        finalResultSet.add(result);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(finalResultSet);
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred here", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    public boolean updateAllocationPayment(String applicationId, String numberType) {
        int updateAllocationPayment;
        String tableName = "";
        String columnConstraintKey = "ApplicationId";
        String numType = (numberType != null ? numberType.toUpperCase() : "");

        try {
            switch (numType) {
                case "NATIONAL":
                case "GEOGRAPHICAL":
                    tableName = "StandardNumber";
                    break;

                case "ISPC":
                    tableName = "IspcNumbers";
                    break;

                case "VANITY":
                case "TOLL-FREE":
                    tableName = "SpecialNumbers";
                    break;

                case "SHORT-CODE":
                    tableName = "ShortCodeNumbers";

                default:
                    break;
            }
            // Update application payment to paid
            updateAllocationPayment = genericTableUpdateRepository.updateTableColumn(tableName,
                    "AllocationPaymentStatus",
                    "Paid",
                    columnConstraintKey,
                    applicationId);
            log.info("Allocation payment response code {}", updateAllocationPayment);

            return updateAllocationPayment >= 1;

        } catch (Exception exe) {
            log.info("Exception occurred during fee calculation {} ", exe.getMessage());
            return false;
        }

    }
}
