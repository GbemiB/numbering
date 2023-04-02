package com.molcom.nms.invoice.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.invoice.dto.InvoiceModel;
import com.molcom.nms.invoice.dto.InvoiceObject;

import java.util.List;

public interface IInvoiceService {

    GenericResponse<InvoiceModel> createInvoice(InvoiceModel model) throws Exception;

    Boolean updateStatus(String transactionRefId) throws Exception;

    GenericResponse<InvoiceObject> getById(int invoiceId) throws Exception;

    InvoiceObject getByIdPlain(int invoiceId) throws Exception;

    GenericResponse<List<InvoiceModel>> getAll(String rowNumber) throws Exception;

    GenericResponse<List<InvoiceModel>> filterForRegularUser(String applicationPaymentStatus, String invoiceType,
                                                             String invoiceNumber,
                                                             String startDate, String endDate,
                                                             String rowNumber) throws Exception;

    GenericResponse<List<InvoiceModel>> filterForRegularUser(String companyName,
                                                             String applicationPaymentStatus, String invoiceType,
                                                             String invoiceNumber,
                                                             String startDate, String endDate,
                                                             String rowNumber) throws Exception;

    GenericResponse<List<InvoiceModel>> filterForAdminUser(String applicationPaymentStatus, String invoiceType,
                                                           String invoiceNumber, String organization,
                                                           String startDate, String endDate,
                                                           String rowNumber) throws Exception;

    int persistInvoice(String applicationId, String subType, Integer amount, String status, boolean isAllocationFee, String feeDescription, String invoiceType) throws Exception;

}
