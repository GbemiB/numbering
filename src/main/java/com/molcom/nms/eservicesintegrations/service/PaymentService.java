package com.molcom.nms.eservicesintegrations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.molcom.nms.eservicesintegrations.dto.DirectPaymentRequest;
import com.molcom.nms.eservicesintegrations.dto.DirectPaymentResponse;
import com.molcom.nms.eservicesintegrations.dto.FinalPaymentRequest;
import com.molcom.nms.eservicesintegrations.dto.PaymentStatusResponse;
import com.molcom.nms.fee.calculation.service.FeeCalculationNew;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.general.utils.RestUtil;
import com.molcom.nms.invoice.service.IInvoiceService;
import com.molcom.nms.jobQueue.scheduler.InvoiceScheduler;
import com.molcom.nms.jobQueue.services.InvoiceProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentService {
    @Autowired
    private final RestUtil restUtil;
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    InvoiceProcessor invoiceProcessor;
    @Autowired
    IInvoiceService invoiceService;
    @Autowired
    private FeeCalculationNew feeCalculationService;
    @Autowired
    private InvoiceScheduler invoiceScheduler;

    @Value("${authorizationPay}")
    private String authorization;
    @Value("${base.urlPay}")
    private String baseUrl;
    @Value("${revenue.service.idPay}")
    private String revenueServiceId;

    /**
     * Service implementation for payment
     *
     * @param restUtil
     */
    public PaymentService(RestUtil restUtil) {
        this.restUtil = restUtil;
    }

    public GenericResponse<DirectPaymentResponse> directPaymentCall(DirectPaymentRequest directPaymentRequest) throws Exception {
        GenericResponse<DirectPaymentResponse> response = new GenericResponse<>();

        FinalPaymentRequest finalRequest = new FinalPaymentRequest();
        finalRequest.setAmount(directPaymentRequest.getAmount());
        finalRequest.setRevenueServiceId(revenueServiceId);
        finalRequest.setCurrencyId(directPaymentRequest.getCurrencyId());
        finalRequest.setCompanyName(directPaymentRequest.getCompanyName());
        finalRequest.setPayerName(directPaymentRequest.getPayerName());
        finalRequest.setPayerAddress(directPaymentRequest.getPayerAddress());
        finalRequest.setPayerPhone(directPaymentRequest.getPayerPhone());
        finalRequest.setPayerEmail(directPaymentRequest.getPayerEmail());
        finalRequest.setDescription(directPaymentRequest.getDescription());
        finalRequest.setGifimisCode(directPaymentRequest.getGifimisCode());
        finalRequest.setApplicationUrl(directPaymentRequest.getApplicationUrl());

        log.info("Final Direct payment request {}", finalRequest);
        try {
            DirectPaymentResponse directPaymentResponse;
            String url = baseUrl + " /finance/payment/intiate-direct-payment/";
            log.info("Request url :: {} ", url);
            log.info("Request url :: {} ", objectMapper.writeValueAsString(finalRequest));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authorization);
            headers.add("Content-Type", "application/json");

            ResponseEntity<String> res = restUtil.setUrl(url).setTimeout(60)
                    .setRequest(finalRequest, headers).post(String.class);

            log.info("AutoFeeResponse from direct payment eservices status code :: {} ", res.getStatusCode());
            log.info("AutoFeeResponse from direct payment eservices body :: {} ", res.getBody());

            directPaymentResponse = objectMapper.readValue(res.getBody(), DirectPaymentResponse.class);

            if (directPaymentResponse != null) {
                // After direct payment
                // Persist application fee/processing fee to invoice table
                int persistResCode = invoiceService.persistInvoice(directPaymentRequest.getApplicationId(),
                        directPaymentRequest.getNumberSubType(), directPaymentRequest.getAmount(), "UNPAID", false, "Application Fee", "APPLICATION");
                log.info("AutoFeeResponse from invoice persist {}", persistResCode);

                response.setResponseCode(ResponseStatus.SUCCESS.getCode());
                response.setResponseCode(ResponseStatus.SUCCESS.getMessage());
                response.setOutputPayload(directPaymentResponse);
            } else {
                response.setResponseCode(ResponseStatus.FAILED.getCode());
                response.setResponseCode(ResponseStatus.FAILED.getMessage());
            }
            return response;

        } catch (Exception exe) {
            log.info("Exception occurred {}", exe.getMessage());
            response.setResponseCode(ResponseStatus.FAILED.getCode());
            response.setResponseCode(ResponseStatus.FAILED.getMessage());
            return response;
        }
    }


//    public GenericResponse<PaymentStatusResponse> checkPaymentStatus(String transactionRefId) throws Exception {
//        GenericResponse<PaymentStatusResponse> response = new GenericResponse<>();
//        try {
//            PaymentStatusResponse paymentStatusResponse;
//            String url = baseUrl + "/finance/payment/direct-status/" + transactionRefId;
//            log.info("Request url :: {} ", url);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Authorization", authorization);
//            headers.add("Content-Type", "application/json");
//
//            ResponseEntity<String> res = restUtil.setUrl(url).setTimeout(60)
//                    .setRequest(headers).get(String.class);
//
//            log.info("AutoFeeResponse from direct payment eservices status code :: {} ", res.getStatusCode());
//            log.info("AutoFeeResponse from direct payment eservices body :: {} ", res.getBody());
//
//            paymentStatusResponse = objectMapper.readValue(res.getBody(), PaymentStatusResponse.class);
//
//            if (paymentStatusResponse != null) {
//                response.setResponseCode(ResponseStatus.SUCCESS.getCode());
//                response.setResponseCode(ResponseStatus.SUCCESS.getMessage());
//                response.setOutputPayload(paymentStatusResponse);
//            } else {
//                response.setResponseCode(ResponseStatus.FAILED.getCode());
//                response.setResponseCode(ResponseStatus.FAILED.getMessage());
//            }
//            return response;
//
//        } catch (Exception exe) {
//            log.info("Exception occurred {}", exe.getMessage());
//            response.setResponseCode(ResponseStatus.FAILED.getCode());
//            response.setResponseCode(ResponseStatus.FAILED.getMessage());
//            return response;
//        }
//    }

    /**
     * Service implementation to check payment status
     *
     * @param transactionRefId
     * @return
     * @throws Exception
     */
    public GenericResponse<PaymentStatusResponse> checkPaymentStatus(String transactionRefId) throws Exception {
        GenericResponse<PaymentStatusResponse> response = new GenericResponse<>();

        try {
            invoiceProcessor.fetchEserviceNotificationQueue();

            response.setResponseCode(ResponseStatus.SUCCESS.getCode());
            response.setResponseCode("Payment status checked but remain unchanged");
            return response;

        } catch (Exception exe) {
            log.info("Exception occurred {}", exe.getMessage());
            response.setResponseCode(ResponseStatus.FAILED.getCode());
            response.setResponseCode(ResponseStatus.FAILED.getMessage());
            return response;
        }
    }
}
