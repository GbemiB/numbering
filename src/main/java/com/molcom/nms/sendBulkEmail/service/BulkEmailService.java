package com.molcom.nms.sendBulkEmail.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.general.utils.RestUtil;
import com.molcom.nms.sendBulkEmail.dto.BulkEmailModel;
import com.molcom.nms.sendBulkEmail.dto.EservicesSendMailRequest;
import com.molcom.nms.sendBulkEmail.repository.IBulkEmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class BulkEmailService implements IBulkEmailService {

    @Autowired
    private final RestUtil restUtil;

    @Autowired
    private IBulkEmailRepository repository;

    @Value("${authorization}")
    private String authorization;

    @Value("${emailUrl}")
    private String emailUrl;

    public BulkEmailService(RestUtil restUtil) {
        this.restUtil = restUtil;
    }

    /**
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<BulkEmailModel> sendEmail(BulkEmailModel model) throws Exception {
        GenericResponse<BulkEmailModel> genericResponse = new GenericResponse<>();
        String mailId = "";
        try {
            mailId = repository.sendEmail(model);
            log.info("Mail Id {} ", mailId);
            if (mailId != null) {
                // Call eservices to send mail
                Boolean isMailSent = callEservicesForMailSending(model);
                log.info("Is Mail Sent {} ", isMailSent);

                if (isMailSent) {
                    // update status of mail to dispatched true if mail was successfully sent self
                    // it remains default of false
                    repository.updateDispatchStatus(mailId);
                }
                // Persist the email request
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());


            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!! ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<BulkEmailModel>> getAll(String rowNumber) throws Exception {
        GenericResponse<List<BulkEmailModel>> genericResponse = new GenericResponse<>();
        try {
            List<BulkEmailModel> bulkEmailModels = repository.getAll(rowNumber);
            if (bulkEmailModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(bulkEmailModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!  {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * @param recipientEmail
     * @param mailSubject
     * @param startDate
     * @param endDate
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<BulkEmailModel>> filter(String recipientEmail, String mailSubject, String startDate, String endDate, String rowNumber) throws Exception {
        GenericResponse<List<BulkEmailModel>> genericResponse = new GenericResponse<>();
        try {
            List<BulkEmailModel> bulkEmailModels = repository.filter(recipientEmail, mailSubject,
                    startDate, endDate, rowNumber);
            if (bulkEmailModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(bulkEmailModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while filtering company representative {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param model
     * @return
     * @throws Exception
     */
    private Boolean callEservicesForMailSending(BulkEmailModel model) throws Exception {
        try {
            EservicesSendMailRequest eservicesSendMail = new EservicesSendMailRequest();
            String url = emailUrl;

            String isDispatched = "";
            log.info("Request coming in::::: {} and url :: {} ", model, url);

            String str = model.getRecipientEmail();
            List<String> emails = Arrays.asList(str.split("\\s*,\\s*"));

            // Assembling http body
            eservicesSendMail.setRecipients(emails);
            eservicesSendMail.setBcc(null);
            eservicesSendMail.setCc(null);
            eservicesSendMail.setBody(model.getMailBody());

            eservicesSendMail.setSubject(model.getMailSubject());

            log.info("eservices send email payload {}", eservicesSendMail);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authorization);
            headers.add("Content-Type", "application/json");

            log.info("Request coming in::::: {} and url :: {} ", model, url);

            ResponseEntity<String> response = restUtil.setUrl(url).setTimeout(60)
                    .setRequest(eservicesSendMail, headers).post(String.class);

            log.info("AutoFeeResponse from eservices status code :: {} ", response.getStatusCode());
            log.info("AutoFeeResponse from eservices body :: {} ", response.getBody());
            String code = response.getStatusCode().toString();
            return code.equals("204 NO_CONTENT");

        } catch (Exception exe) {
            return false;
        }
    }


    /**
     * @param mailRequest
     * @return
     * @throws Exception
     */
    @Override
    public Boolean genericMailFunction(EservicesSendMailRequest mailRequest) throws Exception {
        try {
            String url = emailUrl;
            String isDispatched = "";
            log.info("Request coming in::::: {} and url :: {} ", mailRequest, url);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authorization);
            headers.add("Content-Type", "application/json");

            log.info("Request coming in::::: {} and url :: {} ", mailRequest, url);

            ResponseEntity<String> response = restUtil.setUrl(url).setTimeout(60)
                    .setRequest(mailRequest, headers).post(String.class);

            log.info("AutoFeeResponse from eservices status code :: {} ", response.getStatusCode());
            log.info("AutoFeeResponse from eservices body :: {} ", response.getBody());
            String code = response.getStatusCode().toString();
            return code.equals("204 NO_CONTENT");

        } catch (Exception exe) {
            return false;
        }
    }
}
