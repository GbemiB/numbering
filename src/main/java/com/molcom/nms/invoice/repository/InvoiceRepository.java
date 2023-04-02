package com.molcom.nms.invoice.repository;

import com.molcom.nms.invoice.dto.InvoiceModel;

import java.util.List;

public interface InvoiceRepository {
    int isInvoiceExist(String applicationId) throws Exception;

    int createInvoice(InvoiceModel model) throws Exception;

    int persistZeroFeeInvoiceForMDA(String numberType, String numberSubType,
                                    String applicationId, String organisation,
                                    String initiatorUsername, String initiatorEmail,
                                    String description) throws Exception;

    int updateStatus(String transactionRefId, String status) throws Exception;

    int updateEservicesInvoiceStatus(String eServicesRequestId, String invoiceId) throws Exception;

    InvoiceModel getById(int invoiceId) throws Exception;

    List<InvoiceModel> getAll(String rowNumber) throws Exception;

    List<InvoiceModel> filterForRegularUser(String applicationPaymentStatus, String invoiceType,
                                            String invoiceNumber,
                                            String startDate, String endDate,
                                            String rowNumber) throws Exception;

    List<InvoiceModel> filterForRegularUser(String companyName, String applicationPaymentStatus, String invoiceType,
                                            String invoiceNumber,
                                            String startDate, String endDate,
                                            String rowNumber) throws Exception;

    List<InvoiceModel> filterForAdminUser(String applicationPaymentStatus, String invoiceType,
                                          String invoiceNumber, String organization,
                                          String startDate, String endDate,
                                          String rowNumber) throws Exception;

    List<InvoiceModel> getListOfInvoiceToBeSentToEservices() throws Exception;


    List<InvoiceModel> getListOfInvoiceNotResolvedEservices() throws Exception;

    List<InvoiceModel> getInvoiceForRenewal(String applicationId) throws Exception;

    List<InvoiceModel> getAllocationInvoiceByApplicationId(String applicationId) throws Exception;

    List<InvoiceModel> getAllInvoiceNeededForReporting() throws Exception;

    List<InvoiceModel> getAllocationInvoice(String applicationId);
}
