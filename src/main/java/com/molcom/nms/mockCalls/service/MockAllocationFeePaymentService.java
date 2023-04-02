package com.molcom.nms.mockCalls.service;

import com.molcom.nms.eservicesintegrations.dto.invoice.InvoiceNotificationPayload;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.GetUniqueId;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.invoice.dto.InvoiceModel;
import com.molcom.nms.invoice.repository.InvoiceRepository;
import com.molcom.nms.jobQueue.services.StatusProcessing;
import com.molcom.nms.mockCalls.dto.MockAllocationFeePayment;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Slf4j
@Service
public class MockAllocationFeePaymentService {
    @Autowired
    private StatusProcessing statusProcessing;

    @Autowired
    private InvoiceRepository invoiceRepository;

    /**
     * @param request
     * @return
     * @throws Exception
     */
    public GenericResponse<?> mockAllocationFeePayment(MockAllocationFeePayment request) throws Exception {
        GenericResponse<?> genericResponse = new GenericResponse<>();
        InvoiceModel invoiceToBeTreated = new InvoiceModel();

        try {
            InvoiceNotificationPayload invoiceNotificationPayload = new InvoiceNotificationPayload();
            InvoiceModel invoiceModel = new InvoiceModel();

            List<InvoiceModel> rawModel = invoiceRepository.getAllocationInvoiceByApplicationId(request.getApplicationId());

            if (rawModel.size() >= 1) {
                invoiceToBeTreated = rawModel.get(0);
            }

            Year thisYear = Year.now();
            String random = GetUniqueId.generateRef(6);
            String invoiceNumber = "MOCK/PAYMENT/" + thisYear + "/" + random;
            log.info("Invoice number {} ", invoiceNumber);

            // Build invoice model
            invoiceModel.setInvoiceId(invoiceToBeTreated.getInvoiceId());
            invoiceModel.setNumberType(invoiceToBeTreated.getNumberType());
            invoiceModel.setNumberSubType(invoiceToBeTreated.getNumberSubType());
            invoiceModel.setApplicationId(invoiceToBeTreated.getApplicationId());
            invoiceModel.setOrganization(invoiceToBeTreated.getOrganization());

            log.info("Invoice id {} ", invoiceModel);
            log.info("Invoice id {} ", invoiceModel.getInvoiceId());

            // Mocked
            JSONObject jsonBreakdown = new JSONObject();
            try {
                jsonBreakdown.put("invoiceId", GetUniqueId.getId());
                jsonBreakdown.put("invoiceStatus", "PAID");
                jsonBreakdown.put("invoiceNumber", invoiceNumber);
                jsonBreakdown.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Mocked
            invoiceNotificationPayload.setMessageId(GetUniqueId.getId());
            invoiceNotificationPayload.setReceiptHandle(RefGenerator.getRefNo(100));
            invoiceNotificationPayload.setMD5OfBody(GetUniqueId.getId());
            invoiceNotificationPayload.setBody(jsonBreakdown.toString());

            boolean isPaymentMocked = statusProcessing.processSuccessInvoice(invoiceNotificationPayload, invoiceModel);

            log.info("Is payment mocked status {} ", isPaymentMocked);

            if (isPaymentMocked) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.PAYMENT_MOCK_FAILED.getCode());
                genericResponse.setResponseCode(ResponseStatus.PAYMENT_MOCK_FAILED.getMessage());
            }
        } catch (Exception e) {
            genericResponse.setResponseCode(ResponseStatus.PAYMENT_MOCK_FAILED.getCode());
            genericResponse.setResponseCode(ResponseStatus.PAYMENT_MOCK_FAILED.getMessage());
        }
        return genericResponse;
    }
}
