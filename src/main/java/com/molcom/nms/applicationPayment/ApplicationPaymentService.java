package com.molcom.nms.applicationPayment;

import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableCellGetRepository;
import com.molcom.nms.GenericDatabaseUpdates.repository.IGenericTableUpdateRepository;
import com.molcom.nms.adminmanage.repository.IAdminManagementRepo;
import com.molcom.nms.approvals.repository.ApprovalRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.invoice.repository.InvoiceRepository;
import com.molcom.nms.invoice.service.IInvoiceService;
import com.molcom.nms.sendBulkEmail.dto.EservicesSendMailRequest;
import com.molcom.nms.sendBulkEmail.service.IBulkEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ApplicationPaymentService {
    @Autowired
    private IGenericTableUpdateRepository genericTableUpdateRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private IBulkEmailService bulkEmailService;

    @Autowired
    private IAdminManagementRepo adminManagementRepo;

    @Autowired
    private IInvoiceService invoiceService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private GenericTableCellGetRepository genericTableCellGetRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Service implementation to update application payment
     *
     * @param request
     * @return
     */
    public GenericResponse<String> updateApplicationPayment(ApplicationPaymentRequest request) {
        GenericResponse<String> response = new GenericResponse<>();
        int updateApplicationStatus;
        int updateApplicationPayment;
        int updateTransactionId;
        int updateInvoiceId;
        String tableName = "";
        String columnConstraintKey = "ApplicationId";
        String applicationId = request.getApplicationId();
        String transId = (request.getTransactionId() != null ? request.getTransactionId() : "");
        String isMDA = (request.getIsMDA() != null ? request.getIsMDA() : "");
        log.info("Transaction id {}", transId);

        try {
            switch (request.getNumberSubType().toUpperCase()) {
                case "NATIONAL":
                case "GEOGRAPHICAL":
                    tableName = "StandardNumber";

                    // Update application status to submitted
                    updateApplicationStatus = genericTableUpdateRepository.updateTableColumn(tableName,
                            "ApplicationStatus",
                            "Submitted",
                            columnConstraintKey,
                            applicationId);
                    log.info("Application status response code {}", updateApplicationStatus);

                    // Update application payment to paid
                    updateApplicationPayment = genericTableUpdateRepository.updateTableColumn(tableName,
                            "ApplicationPaymentStatus",
                            "Paid",
                            columnConstraintKey,
                            applicationId);
                    log.info("Application payment response code {}", updateApplicationPayment);

                    // Update transaction id
                    updateTransactionId = genericTableUpdateRepository.updateTableColumn(tableName,
                            "ApplicationPaymentTransId",
                            transId,
                            columnConstraintKey,
                            applicationId);
                    log.info("Application Id response code {}", updateTransactionId);

                    log.info("is mda {}", isMDA);
                    // If MDA
                    if (Objects.equals(isMDA, "TRUE")) {
                        String num = genericTableCellGetRepository.getNumberType(applicationId);
                        String subNum = genericTableCellGetRepository.getNumberSubType(applicationId);
                        String compName = genericTableCellGetRepository.getCompanyNameOfApp(applicationId);
                        if (num != null && subNum != null && compName != null) {
                            invoiceRepository.persistZeroFeeInvoiceForMDA(num, subNum, applicationId,
                                    compName, compName, compName,
                                    subNum + " number application fee");
                        }
                    }

                    // If not MDA
                    if (Objects.equals(isMDA, "FALSE")) {
                        // update payment status to paid in invoice table
                        updateInvoiceId = genericTableUpdateRepository.updateTableColumn("Invoice",
                                "PaymentStatus",
                                "PAID",
                                "ApplicationId",
                                applicationId);
                        log.info("Update invoice status {}", updateInvoiceId);
                    }

                    if ((Objects.equals(updateApplicationStatus, 1))
                            && (Objects.equals(updateApplicationPayment, 1))
                            && (Objects.equals(updateTransactionId, 1))) {
                        response.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        response.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                    } else {
                        response.setResponseCode(ResponseStatus.FAILED.getCode());
                        response.setResponseMessage("Failed to update application payment status");
                    }
                    return response;

                case "VANITY":
                case "TOLL-FREE":
                    tableName = "SpecialNumbers";

                    // Update application status to submitted
                    updateApplicationStatus = genericTableUpdateRepository.updateTableColumn(tableName,
                            "ApplicationStatus",
                            "Submitted",
                            columnConstraintKey,
                            applicationId);
                    log.info("Application status response code {}", updateApplicationStatus);

                    // Update application payment to paid
                    updateApplicationPayment = genericTableUpdateRepository.updateTableColumn(tableName,
                            "ApplicationPaymentStatus",
                            "Paid",
                            columnConstraintKey,
                            applicationId);
                    log.info("Application payment response code {}", updateApplicationPayment);

                    // Update transaction id
                    updateTransactionId = genericTableUpdateRepository.updateTableColumn(tableName,
                            "ApplicationPaymentTransId",
                            transId,
                            columnConstraintKey,
                            applicationId);
                    log.info("Application Id response code {}", updateTransactionId);

                    // If MDA
                    if (Objects.equals(isMDA, "TRUE")) {
                        String num = genericTableCellGetRepository.getNumberType(applicationId);
                        String subNum = genericTableCellGetRepository.getNumberSubType(applicationId);
                        String compName = genericTableCellGetRepository.getCompanyNameOfApp(applicationId);
                        if (num != null && subNum != null && compName != null) {
                            invoiceRepository.persistZeroFeeInvoiceForMDA(num, subNum, applicationId,
                                    compName, compName, compName,
                                    subNum + " number application fee");
                        }
                    }

                    // If not MDA
                    if (Objects.equals(isMDA, "FALSE")) {
                        // update payment status to paid in invoice table
                        updateInvoiceId = genericTableUpdateRepository.updateTableColumn("Invoice",
                                "PaymentStatus",
                                "PAID",
                                "ApplicationId",
                                applicationId);
                        log.info("Update invoice status {}", updateInvoiceId);
                    }

                    if ((Objects.equals(updateApplicationStatus, 1))
                            && (Objects.equals(updateApplicationPayment, 1))
                            && (Objects.equals(updateTransactionId, 1))) {
                        response.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        response.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                    } else {
                        response.setResponseCode(ResponseStatus.FAILED.getCode());
                        response.setResponseMessage("Failed to update application payment status");
                    }
                    return response;

                case "SHORT-CODE":
                    tableName = "ShortCodeNumbers";

                    // NOTE: NO AUTO GENERATION OF INVOICE FOR SHORT CODE

                    // Update application status to submitted
                    updateApplicationStatus = genericTableUpdateRepository.updateTableColumn(tableName,
                            "ApplicationStatus",
                            "Submitted",
                            columnConstraintKey,
                            applicationId);
                    log.info("Application status response code {}", updateApplicationStatus);

                    // Update application payment to paid
                    updateApplicationPayment = genericTableUpdateRepository.updateTableColumn(tableName,
                            "ApplicationPaymentStatus",
                            "Paid",
                            columnConstraintKey,
                            applicationId);
                    log.info("Application payment response code {}", updateApplicationPayment);

                    // Update transaction id
                    updateTransactionId = genericTableUpdateRepository.updateTableColumn(tableName,
                            "ApplicationPaymentTransId",
                            transId,
                            columnConstraintKey,
                            applicationId);
                    log.info("Application Id response code {}", updateTransactionId);

                    // If MDA
                    if (Objects.equals(isMDA, "TRUE")) {
                        String num = genericTableCellGetRepository.getNumberType(applicationId);
                        String subNum = genericTableCellGetRepository.getNumberSubType(applicationId);
                        String compName = genericTableCellGetRepository.getCompanyNameOfApp(applicationId);
                        if (num != null && subNum != null && compName != null) {
                            invoiceRepository.persistZeroFeeInvoiceForMDA(num, subNum, applicationId,
                                    compName, compName, compName,
                                    subNum + " NUMBER APPLICATION FEE");
                        }
                    }

                    // If not MDA
                    if (Objects.equals(isMDA, "FALSE")) {
                        // update payment status to paid in invoice table
                        updateInvoiceId = genericTableUpdateRepository.updateTableColumn("Invoice",
                                "PaymentStatus",
                                "PAID",
                                "ApplicationId",
                                applicationId);
                        log.info("Update invoice status {}", updateInvoiceId);
                    }
                    if ((Objects.equals(updateApplicationStatus, 1))
                            && (Objects.equals(updateApplicationPayment, 1))
                            && (Objects.equals(updateTransactionId, 1))) {
                        response.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        response.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                    } else {
                        response.setResponseCode(ResponseStatus.FAILED.getCode());
                        response.setResponseMessage("Failed to update application payment status");
                    }
                    return response;

                case "ISPC":
                    // NOTE: NO APPLICATION FEE AND UPDATE OF APPLICATION FEE PAYMENT FOR ISPC
                    // NOTE: NO AUTO GENERATION OF INVOICE FOR SHORT CODE
                    tableName = "IspcNumbers";

                    // Update application status to submitted
                    updateApplicationStatus = genericTableUpdateRepository.updateTableColumn(tableName,
                            "ApplicationStatus",
                            "Submitted",
                            columnConstraintKey,
                            applicationId);
                    log.info("Application status response code {}", updateApplicationStatus);

                    if ((Objects.equals(updateApplicationStatus, 1))) {
                        response.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        response.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                    } else {
                        response.setResponseCode(ResponseStatus.FAILED.getCode());
                        response.setResponseMessage("Failed to update application payment status");
                    }
                    return response;

                default:
                    break;
            }

            // send a mail to overall collaborator to assign admin to application
            boolean isMailSent = sendMailToOverAllCollaborator(request.getNumberType(), request.getApplicationId());
            log.info("Is mail sent {}", isMailSent);


        } catch (Exception exe) {
            log.info("Exception occurred during fee calculation {} ", exe.getMessage());
        }
        return null;

    }


    /**
     * Mail section
     * This method send email to the user approving the next workflow step
     * This method is called after a successful approval of a workflow step
     *
     * @param applicationId@return
     * @throws SQLException
     */
    @Async
    boolean sendMailToOverAllCollaborator(String numberType, String applicationId) throws SQLException {
        EservicesSendMailRequest adminMailRequest = new EservicesSendMailRequest();
        EservicesSendMailRequest userMailRequest = new EservicesSendMailRequest();

        String numType = genericTableCellGetRepository.getNumberType(applicationId);
        String numSubType = genericTableCellGetRepository.getNumberSubType(applicationId);
        String companyNameVal = genericTableCellGetRepository.getCompanyNameOfApp(applicationId);
        String companyEmail = genericTableCellGetRepository.getCompanyEmail(applicationId);

        // Send mail to overall admin
        List<String> adminEmailsVal = adminManagementRepo.getCollaboratorAdmins();
        adminMailRequest.setRecipients(adminEmailsVal); // should be email
        adminMailRequest.setSubject("ASSIGN ADMIN TO APPLICATION");
        adminMailRequest.setBody("<h3>An application require assignment to admins: See details below <h3/> " +
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

        // send mail to user that assignment to admin has started
        List<String> companyEmails = new ArrayList<>();
        assert companyEmails != null;
        companyEmails.add(companyEmail);
        userMailRequest.setRecipients(companyEmails); // should be email
        userMailRequest.setSubject("HELLO " + companyNameVal + ",");
        userMailRequest.setBody("<h3>Congratulations!, Your number application with application ID " + applicationId +
                " has been reviewed successfully and is been assigned to admins <h3/> ");
        userMailRequest.setCc(null);
        userMailRequest.setBcc(null);

        try {
            Boolean isMailSentToAdmin = bulkEmailService.genericMailFunction(adminMailRequest);
            Boolean isMailSentToUser = bulkEmailService.genericMailFunction(userMailRequest);
            log.info("Is Mail Sent To Admin {}", isMailSentToAdmin);
            log.info("Is Mail Sent To User{}", isMailSentToUser);

            return isMailSentToUser && isMailSentToAdmin;
        } catch (Exception ex) {
            log.info("Exception occurred while sending mail {}", ex.getMessage());
        }
        return false;
    }
}
