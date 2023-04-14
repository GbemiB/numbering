package com.molcom.nms.jobQueue.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableUpdateRepository;
import com.molcom.nms.eservicesintegrations.dto.invoice.InvoiceNotificationPayload;
import com.molcom.nms.eservicesintegrations.service.EInvoiceService;
import com.molcom.nms.invoice.dto.InvoiceModel;
import com.molcom.nms.number.renewal.repository.INumRenewalRepository;
import com.molcom.nms.number.selectedNumber.repository.ISelectedNumbersRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Map;

@Service
@Slf4j
public class StatusProcessing {

    @Autowired
    private GenericTableUpdateRepository genericTableUpdateRepository;

    @Autowired
    private EInvoiceService eInvoiceService;

    @Autowired
    private ISelectedNumbersRepo selectedNumbersRepo;

    @Autowired
    private INumRenewalRepository numRenewalRepository;

    /**
     * Method to process failed invoice
     *
     * @param invoiceNotificationPayload
     * @param invoiceModel
     * @return
     * @throws SQLException
     */
    public boolean processFailedInvoice(InvoiceNotificationPayload invoiceNotificationPayload,
                                        InvoiceModel invoiceModel) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(invoiceNotificationPayload.getBody(), Map.class);
        String invoiceStatus = data.get("invoiceStatus");

        // step 1: Update the invoice status to failed
        int updatePaymentStatus = genericTableUpdateRepository.updateTableColumn(
                "Invoice",
                "PaymentStatus",
                "FAILED",
                "InvoiceId",
                invoiceModel.getInvoiceId()
        );

        // step 3: Update `IsInvoiceResolvedByEservices` flag to `TRUE` in table `Invoice`
        // so it's processing will stop
        int updateResolvedStatus = genericTableUpdateRepository.updateTableColumn(
                "Invoice",
                "IsInvoiceResolvedByEservices",
                "TRUE",
                "InvoiceId",
                invoiceModel.getInvoiceId()
        );
        log.info("Invoice IsInvoiceResolvedByEservices status update for invoice number {} is {} : ",
                invoiceModel.getInvoiceNumber(), updateResolvedStatus);

        // step 3: Check if application is in renewal table to know if it for renewal, update payment status to count
        int count = numRenewalRepository.countIfNoExist(invoiceModel.getApplicationId());
        if (count >= 1) {
            int updateRenewalStatus = genericTableUpdateRepository.updateTableColumn(
                    "NumberRenewal",
                    "PaymentStatus",
                    invoiceStatus,
                    "ApplicationId",
                    invoiceModel.getApplicationId()
            );
            log.info("Invoice payment status is updated for renewal application {} status is {} : ",
                    invoiceModel.getApplicationId(), updateRenewalStatus);
        }

        return updatePaymentStatus >= 1;

    }

    /**
     * Method to process paid/successful invoice
     *
     * @param invoiceNotificationPayload
     * @param invoiceModel
     * @return
     * @throws SQLException
     */
    public boolean processSuccessInvoice(InvoiceNotificationPayload invoiceNotificationPayload,
                                         InvoiceModel invoiceModel) throws Exception {

        // step 1: Update the invoice status to paid
        int updateEServicestatusOne = genericTableUpdateRepository.updateTableColumn(
                "Invoice",
                "PaymentStatus",
                "PAID",
                "InvoiceId",
                invoiceModel.getInvoiceId()
        );

        // update the invoice status
        int updateEServicestatusTwo = genericTableUpdateRepository.updateTableColumn(
                "Invoice",
                "Status",
                "PAID",
                "InvoiceId",
                invoiceModel.getInvoiceId()
        );
        log.info("Eservices invoice response {} ", updateEServicestatusOne);
        log.info("Eservices invoice response {} ", updateEServicestatusTwo);

        // step 2: Update the invoice number to eservice invoice id
        // Using mapper to get the invoice status
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(invoiceNotificationPayload.getBody(), Map.class);
        String invoiceNumber = data.get("invoiceNumber");
        String invoiceStatus = data.get("invoiceStatus");

        if (invoiceNumber != null) {
            int updateInvoiceNumber = genericTableUpdateRepository.updateTableColumn(
                    "Invoice",
                    "InvoiceNumber",
                    invoiceNumber,
                    "InvoiceId",
                    invoiceModel.getInvoiceId()
            );
            log.info("Update of invoice number status {} ", updateInvoiceNumber);
        }

        // step 3: Update selected numbers for the invoice as allocated and stamp validity period
        int updatedSelectedNumber = selectedNumbersRepo.updateAfterAllocation
                (invoiceModel.getApplicationId(), invoiceModel.getOrganization());
        log.info("Selected number allocation status updated for application id {} is : {} ",
                invoiceModel.getApplicationId(), updatedSelectedNumber);

        // step 4: Update allocation payment status to PAID
        boolean updateAllocationPayment = updateAllocationPayment(invoiceModel.getApplicationId(),
                invoiceModel.getNumberSubType(),
                "PAID");
        log.info("Allocation status updated for application id {} with invoice number {} is : {} ",
                invoiceModel.getApplicationId(), invoiceModel.getInvoiceNumber(), updateAllocationPayment);

        // step 6: Update `IsInvoiceResolvedByEservices` flag to `TRUE` in table `Invoice`
        // so it's processing will stop
        int updateResolvedStatus = genericTableUpdateRepository.updateTableColumn(
                "Invoice",
                "IsInvoiceResolvedByEservices",
                "TRUE",
                "InvoiceId",
                invoiceModel.getInvoiceId()
        );
        log.info("Invoice IsInvoiceResolvedByEservices status update for invoice number {} is {} : ",
                invoiceModel.getInvoiceNumber(), updateResolvedStatus);

        // step 7: Check if application is in renewal table to know if it for renewal, update payment status to count
        int count = numRenewalRepository.countIfNoExist(invoiceModel.getApplicationId());
        if (count >= 1) {
            int updateRenewalStatus = genericTableUpdateRepository.updateTableColumn(
                    "NumberRenewal",
                    "PaymentStatus",
                    "PAID",
                    "ApplicationId",
                    invoiceModel.getApplicationId()
            );
            log.info("Invoice payment status is updated for renewal application {} status is {} : ",
                    invoiceModel.getApplicationId(), updateRenewalStatus);
        }
        return updateEServicestatusOne >= 1;
    }

    /**
     * Method to process pending invoice
     *
     * @param invoiceNotificationPayload
     * @param invoiceModel
     * @return
     * @throws SQLException
     */
    public boolean processPendingInvoice(InvoiceNotificationPayload invoiceNotificationPayload,
                                         InvoiceModel invoiceModel) throws SQLException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(invoiceNotificationPayload.getBody(), Map.class);
        String invoiceNumber = data.get("invoiceNumber");
        String invoiceStatus = data.get("invoiceStatus");

        if (invoiceNumber != null) {
            int updateInvoiceNumber = genericTableUpdateRepository.updateTableColumn(
                    "Invoice",
                    "InvoiceNumber",
                    invoiceNumber,
                    "InvoiceId",
                    invoiceModel.getInvoiceId()
            );

            // step 2: Check if application is in renewal table to know if it for renewal, update payment status to count
            int count = numRenewalRepository.countIfNoExist(invoiceModel.getApplicationId());
            if (count >= 1) {
                int updateRenewalStatus = genericTableUpdateRepository.updateTableColumn(
                        "NumberRenewal",
                        "PaymentStatus",
                        invoiceStatus,
                        "ApplicationId",
                        invoiceModel.getApplicationId()
                );
                log.info("Invoice payment status is updated for renewal application {} status is {} : ",
                        invoiceModel.getApplicationId(), updateRenewalStatus);
            }

            // NOTE: Don't delete invoice since it's still processing
            return updateInvoiceNumber >= 1;

        }
        return true;
    }


    /**
     * Method to update application allocation payment status
     *
     * @param applicationId
     * @param numSubType
     * @return
     */
    public boolean updateAllocationPayment(String applicationId, String numSubType, String status) {
        int updateAllocationPayment;
        String tableName = "";
        String columnConstraintKey = "ApplicationId";
        String numSub = (numSubType != null ? numSubType.toUpperCase() : "");
        String stat = (status != null ? status.toUpperCase() : "");

        try {
            switch (numSub) {
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
                    stat,
                    columnConstraintKey,
                    applicationId);
            log.info("Allocation payment response code {}", updateAllocationPayment);

            return updateAllocationPayment >= 1;

        } catch (Exception exe) {
            log.info("Exception occurred during update of allocation {} ", exe.getMessage());
            return false;
        }

    }

}
