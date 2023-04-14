package com.molcom.nms.jobQueue.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableCellGetRepository;
import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableUpdateRepository;
import com.molcom.nms.eservicesintegrations.dto.invoice.InvoiceNotificationPayload;
import com.molcom.nms.eservicesintegrations.service.EInvoiceService;
import com.molcom.nms.fee.calculation.dto.FeeCalculationRequest;
import com.molcom.nms.fee.calculation.dto.FeeCalculationResponse;
import com.molcom.nms.fee.calculation.service.FeeCalculatorRenewal;
import com.molcom.nms.fee.calculation.service.FeeCalculatorShortCodeRenewal;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.invoice.dto.InvoiceModel;
import com.molcom.nms.invoice.dto.InvoiceObject;
import com.molcom.nms.invoice.repository.InvoiceRepository;
import com.molcom.nms.invoice.service.IInvoiceService;
import com.molcom.nms.number.renewal.dto.NumRenewalModel;
import com.molcom.nms.number.renewal.repository.INumRenewalRepository;
import com.molcom.nms.number.selectedNumber.dto.DistinctNumber;
import com.molcom.nms.number.selectedNumber.repository.ISelectedNumbersRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class InvoiceProcessor {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private IInvoiceService invoiceService;

    @Autowired
    private EInvoiceService eInvoiceService;

    @Autowired
    private ISelectedNumbersRepo selectedNumbersRepo;

    @Autowired
    private FeeCalculatorRenewal feeCalculatorRenewal;

    @Autowired
    private FeeCalculatorShortCodeRenewal feeCalculatorShortCodeRenewal;

    @Autowired
    private GenericTableUpdateRepository genericTableUpdateRepository;

    @Autowired
    private GenericTableCellGetRepository genericTableCellGetRepository;

    @Autowired
    private StatusProcessing statusProcessing;

    @Autowired
    private INumRenewalRepository numRenewalRepository;

    /**
     * Method to get all invoice with flag `ShouldSendToEservices` as 'TRUE'
     * For each of the invoice, it makes call to eservices to create invoice with eservices
     * on successful call, invoice field `EservicesRequestId` is updated from eservices response object
     * and `ShouldSendToEservices` is updated to 'FALSE'
     *
     * @throws Exception
     */
    public void sendInvoiceToEserviceJob() throws Exception {
        List<InvoiceModel> invoiceModelList = invoiceRepository.getListOfInvoiceToBeSentToEservices();
        log.info("List of invoices to be sent to eservices {}", invoiceModelList);

        invoiceModelList.forEach(inv -> {
            try {
                assert inv.getInvoiceId() != null;
                String invoiceIdVal = (inv.getInvoiceId() != null ? inv.getInvoiceId() : "");
                InvoiceObject object = invoiceService.getByIdPlain(Integer.parseInt(invoiceIdVal));
                log.info("Get invoice object {}", object);

                // invoice type can either be "NEW", RENEWAL", "APPLICATION"
                // Only "NEW" and "RENEWAL" is sent to eservices
                assert object != null;
                assert object.getInvoiceModel() != null;
                boolean sent = eInvoiceService.sendInvoiceToEservices(object, object.getInvoiceModel().getInvoiceType());
                log.info("Sent to eservices {}", sent);

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    /**
     * Method to call eservices to get notification queue
     * It check if all not resolved invoices is in the notification queue
     *
     * @throws Exception
     */
    public void fetchEserviceNotificationQueue() throws Exception {
        List<InvoiceNotificationPayload> queue = eInvoiceService.fetchEserviceNotificationQueue();
        log.info("Eservice invoice notification queue {} ", queue);

        List<InvoiceModel> notResolved = invoiceRepository.getListOfInvoiceNotResolvedEservices();

        notResolved.forEach(notResolvedVal -> {
            // Get out the invoice id persisted for an invoice after sent to eservice from
            // pull or notification queue
            String invoiceIdDb = notResolvedVal.getEservicesRequestId();

            if (queue != null) {
                queue.forEach(q -> {
//                    log.info("current queue {} ", q);
                    // Using mapper to get the invoice status
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, String> data = null;
                    try {
                        data = mapper.readValue(q.getBody(), Map.class);
                        String invoiceIdVal = data.get("invoiceId") != null ? data.get("invoiceId") : "";
                        String invoiceStatus = data.get("invoiceStatus") != null ? data.get("invoiceStatus") : "";
                        String receiptHandle = data.get("ReceiptHandle") != null ? data.get("ReceiptHandle") : "";

                        log.info("Invoice id from eservices {} ", invoiceIdVal);
                        log.info("Receipt Handle from eservices {} ", receiptHandle);
                        log.info("Invoice id kept in db  {} ", invoiceIdDb);

                        log.info("Invoice status {} ", invoiceStatus);

                        if (Objects.equals(invoiceIdVal, invoiceIdDb)) {
                            if (invoiceStatus != null) {
                                if (Objects.equals(invoiceStatus, "FAILED")) {
                                    try {
                                        // PROCESS FOR FAILED
                                        boolean status = statusProcessing.processFailedInvoice(q, notResolvedVal);
                                        log.info("Status for failed invoice processing {} ", status);

                                        // update the invoice status in eservices
                                        int updateEServicestatusOne = genericTableUpdateRepository.updateTableColumn(
                                                "Invoice",
                                                "Status",
                                                invoiceStatus,
                                                "InvoiceId",
                                                notResolvedVal.getInvoiceId()
                                        );

                                        // update the invoice status in eservices
                                        int updateEServicestatusTwo = genericTableUpdateRepository.updateTableColumn(
                                                "Invoice",
                                                "PaymentStatus",
                                                invoiceStatus,
                                                "InvoiceId",
                                                notResolvedVal.getInvoiceId()
                                        );
                                        log.info("Eservices invoice response {} ", updateEServicestatusOne);
                                        log.info("Eservices invoice response {} ", updateEServicestatusTwo);

                                        // Delete from notification queue
                                        boolean isDeleted = eInvoiceService.deleteFromNotificationQueue(receiptHandle);
                                        log.info("Invoice deleted from notification pull {} for invoice id {} because status is {} ",
                                                isDeleted, invoiceIdVal, invoiceStatus);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (Objects.equals(invoiceStatus, "PAID")) {

                                    try {
                                        // PROCESS FOR PAID
                                        boolean status = statusProcessing.processSuccessInvoice(q, notResolvedVal);
                                        log.info("Status for paid invoice processing {} ", status);

                                        // Delete from notification queue
                                        boolean isDeleted = eInvoiceService.deleteFromNotificationQueue(receiptHandle);
                                        log.info("Invoice deleted from notification pull {} for invoice id {} because status is {} ",
                                                isDeleted, invoiceIdVal, invoiceStatus);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if ((Objects.equals(invoiceStatus, "DUPLICATE_INVOICE")) ||
                                        (Objects.equals(invoiceStatus, "OVER_PAID")) ||
                                        (Objects.equals(invoiceStatus, "PARTLY_PAID")) ||
                                        (Objects.equals(invoiceStatus, "FINANCE_REJECTED")) ||
                                        (Objects.equals(invoiceStatus, "CANCELLED"))) {
                                    try {
                                        // OTHER STATUSES
                                        boolean status = statusProcessing.processFailedInvoice(q, notResolvedVal);
                                        log.info("Other Status invoice processing {} ", status);

                                        // update the invoice status
                                        int updateEServicestatusOne = genericTableUpdateRepository.updateTableColumn(
                                                "Invoice",
                                                "Status",
                                                invoiceStatus,
                                                "InvoiceId",
                                                notResolvedVal.getInvoiceId()
                                        );
                                        // update the invoice status
                                        int updateEServicestatusTwo = genericTableUpdateRepository.updateTableColumn(
                                                "Invoice",
                                                "PaymentStatus",
                                                invoiceStatus,
                                                "InvoiceId",
                                                notResolvedVal.getInvoiceId()
                                        );
                                        log.info("Eservices invoice response {} ", updateEServicestatusOne);
                                        log.info("Eservices invoice response {} ", updateEServicestatusTwo);


                                        // Update the application status to the status returned from eservices
                                        boolean statusUpdate = statusProcessing.updateAllocationPayment(notResolvedVal.getApplicationId(),
                                                notResolvedVal.getNumberSubType(), invoiceStatus);

                                        log.info("Status update {} ", statusUpdate);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        // PROCESS FOR PENDING
                                        boolean status = statusProcessing.processPendingInvoice(q, notResolvedVal);
                                        log.info("Status for failed invoice processing {} ", status);

                                        // update the invoice status
                                        int updateEServicestatusOne = genericTableUpdateRepository.updateTableColumn(
                                                "Invoice",
                                                "Status",
                                                invoiceStatus,
                                                "InvoiceId",
                                                notResolvedVal.getInvoiceId()
                                        );

                                        // update the invoice status
                                        int updateEServicestatusTwo = genericTableUpdateRepository.updateTableColumn(
                                                "Invoice",
                                                "PaymentStatus",
                                                invoiceStatus,
                                                "InvoiceId",
                                                notResolvedVal.getInvoiceId()
                                        );
                                        log.info("Eservices invoice response {} ", updateEServicestatusOne);
                                        log.info("Eservices invoice response {} ", updateEServicestatusTwo);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }


                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }


                });
            }

        });
    }

    /**
     * Method to check for expired application and set to for renewal process to begin
     *
     * @throws Exception
     */
    public void updateExpiredApplicationForRenewal() throws Exception {
        int res = selectedNumbersRepo.updateApplicationForRenewal();
        log.info("Expired application update status {} ", res);
    }

    /**
     * Method to call check for expired application
     * and send to invoice for allocation fee invoice generation based on number type sending renewal as number type
     * persist the application in renewal table
     *
     * @throws Exception
     */
    public void checkForExpiredApplications() throws Exception {
        FeeCalculationRequest feeCalculationRequest = new FeeCalculationRequest();
        // Get expired applications
        List<DistinctNumber> expiredNumber = selectedNumbersRepo.getExpiredDistinct();
        log.info("Expired applications are : {} ", expiredNumber);

        if (expiredNumber != null) {
            // Generate invoice for expired application
            expiredNumber.forEach(app -> {
                String companyName = genericTableCellGetRepository.getCompanyNameOfApp(app.getApplicationId());
                String numberType = genericTableCellGetRepository.getNumberType(app.getApplicationId());
                String subType = genericTableCellGetRepository.getNumberSubType(app.getApplicationId());

                // If application is standard or special
                if ((Objects.equals(app.getNumberType().toUpperCase(), "STANDARD")) ||
                        (Objects.equals(app.getNumberType().toUpperCase(), "SPECIAL"))) {

                    // update the application type to renewal in application table
                    assert app.getNumberType() != null;
                    if (Objects.equals(app.getNumberType().toUpperCase(), "STANDARD")) {
                        try {
                            // save the application in renewal table
                            NumRenewalModel numRenewalModel = new NumRenewalModel();
                            numRenewalModel.setApplicationId(app.getApplicationId());
                            numRenewalModel.setOrganization(companyName);
                            numRenewalModel.setRenewalStatus("APPROVED");
                            numRenewalModel.setPaymentStatus("AWAITING REVIEW");
                            numRenewalModel.setNumberType("STANDARD");
                            numRenewalModel.setNumberSubType(subType);
                            int updateInRenewalTable = numRenewalRepository.save(numRenewalModel);
                            log.info(app.getApplicationId() + " is persisted in renewal table successfully {} ", updateInRenewalTable);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    if (Objects.equals(app.getNumberType().toUpperCase(), "SPECIAL")) {
                        try {
                            // save the application in renewal table
                            NumRenewalModel numRenewalModel = new NumRenewalModel();
                            numRenewalModel.setApplicationId(app.getApplicationId());
                            numRenewalModel.setOrganization(companyName);
                            numRenewalModel.setRenewalStatus("APPROVED");
                            numRenewalModel.setPaymentStatus("AWAITING REVIEW");
                            numRenewalModel.setNumberType("SPECIAL");
                            numRenewalModel.setNumberSubType(subType);
                            int updateInRenewalTable = numRenewalRepository.save(numRenewalModel);
                            log.info(app.getApplicationId() + " is persisted in renewal table successfully {} ", updateInRenewalTable);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    // calculate the renewal fees
                    feeCalculationRequest.setApplicationId(app.getApplicationId());
                    feeCalculationRequest.setNumberType(app.getNumberType().toUpperCase());
                    feeCalculationRequest.setNumberSubType(app.getNumberSubType().toUpperCase());
                    try {
                        GenericResponse<FeeCalculationResponse> renewalFee =
                                feeCalculatorRenewal.feeRenewalCalculator(feeCalculationRequest);
                        log.info("Renewal Fee {} : ", renewalFee);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // If application is short code
                } else if (Objects.equals(app.getNumberType().toUpperCase(), "SHORT-CODE")) {
                    try {

                        // save the application in renewal table
                        NumRenewalModel numRenewalModel = new NumRenewalModel();
                        numRenewalModel.setApplicationId(app.getApplicationId());
                        numRenewalModel.setOrganization(companyName);
                        numRenewalModel.setRenewalStatus("APPROVED");
                        numRenewalModel.setPaymentStatus("AWAITING REVIEW");
                        numRenewalModel.setNumberType("SHORT-CODE");
                        numRenewalModel.setNumberSubType(subType);
                        int updateInRenewalTable = numRenewalRepository.save(numRenewalModel);
                        log.info(app.getApplicationId() + " is persisted in renewal table successfully {} ", updateInRenewalTable);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        feeCalculatorShortCodeRenewal.generateShortCodeRenewal(app.getApplicationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (Objects.equals(app.getNumberType().toUpperCase(), "ISPC")) {
                    try {

                        // save the application in renewal table
                        // No payment for renewal
                        NumRenewalModel numRenewalModel = new NumRenewalModel();
                        numRenewalModel.setApplicationId(app.getApplicationId());
                        numRenewalModel.setOrganization(companyName);
                        numRenewalModel.setRenewalStatus("APPROVED");
                        numRenewalModel.setPaymentStatus("NO PAYMENT");
                        numRenewalModel.setNumberType("ISPC");
                        numRenewalModel.setNumberSubType(subType);
                        int updateInRenewalTable = numRenewalRepository.save(numRenewalModel);
                        log.info(app.getApplicationId() + " is persisted in renewal table successfully {} ", updateInRenewalTable);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
