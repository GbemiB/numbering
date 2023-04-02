package com.molcom.nms.numberReport.ispc.service;

import com.molcom.nms.adminmanage.repository.IAdminManagementRepo;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.numberReport.ispc.dto.BulkUploadItem;
import com.molcom.nms.numberReport.ispc.dto.BulkUploadReportIspc;
import com.molcom.nms.numberReport.ispc.dto.BulkUploadResponse;
import com.molcom.nms.numberReport.ispc.dto.ReportIspcNoModel;
import com.molcom.nms.numberReport.ispc.repository.ReportIspcRepository;
import com.molcom.nms.sendBulkEmail.dto.EservicesSendMailRequest;
import com.molcom.nms.sendBulkEmail.service.IBulkEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportIspcService {
    @Autowired
    private ReportIspcRepository repository;

    @Autowired
    private IAdminManagementRepo adminManagementRepo;

    @Autowired
    private IBulkEmailService bulkEmailService;

    /**
     * @param model
     * @return
     */
    public GenericResponse<ReportIspcNoModel> createSingleNumber(ReportIspcNoModel model) {
        log.info("RequestBody", model);
        GenericResponse<ReportIspcNoModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            int count = repository.countIfNoExist(model.getIspcNumber());
            log.info("Checking if number already exist {} ", count);
            if (count >= 1) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage(ResponseStatus.ALREADY_EXIST.getMessage());
            } else {
                responseCode = repository.createSingleNumber(model);
                log.info("AutoFeeResponse code for saving new area code {} ", responseCode);
                if (responseCode == 1) {
                    genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                } else {
                    genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                }
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!! ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        log.info("AutoFeeResponse", genericResponse);
        return genericResponse;
    }

    /**
     * @param startDate
     * @param endDate
     * @param organisation
     * @param rowNumber
     * @return
     */
    public GenericResponse<List<ReportIspcNoModel>> filterNumber(String startDate, String endDate, String organisation, String rowNumber) {
        GenericResponse<List<ReportIspcNoModel>> genericResponse = new GenericResponse<>();
        try {
            List<ReportIspcNoModel> createIspcNoModelList = repository.filterNumber(startDate, endDate, organisation, rowNumber);
            log.info("Result set from repository {} ====> ", createIspcNoModelList);
            if (createIspcNoModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(createIspcNoModelList);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * @param bulkUploadRequest
     * @return
     */
    public GenericResponse<BulkUploadResponse> createBulkNumber(BulkUploadReportIspc bulkUploadRequest) {
        GenericResponse<BulkUploadResponse> genericResponse = new GenericResponse<>();
        try {
            BulkUploadResponse response = repository.createBulkNumber(bulkUploadRequest);
            log.info("Bulk upload {} ", response);
            if (response != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(response);
            } else {
                BulkUploadResponse resp = new BulkUploadResponse();
                resp.setTotalCount(String.valueOf(bulkUploadRequest.getBulkList().size()));
                resp.setBatchId(RefGenerator.getRefNo(5));
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                genericResponse.setOutputPayload(resp);
            }

            sendMailIfBulkUploadFails(response, bulkUploadRequest);

        } catch (Exception exception) {
            log.info("Exception occurred !!!!!!!! ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    // Mail section
    @Async
    boolean sendMailIfBulkUploadFails(BulkUploadResponse bulkUploadResponse, BulkUploadReportIspc bulkUploadRequest) {
        // make it async for optimal performance and line separator
        List<BulkUploadItem> success = bulkUploadResponse.getAllList().stream().filter(item -> item.getItemResCode().contains("000"))
                .collect(Collectors.toList());
        List<BulkUploadItem> duplicate = bulkUploadResponse.getAllList().stream().filter(item -> item.getItemResCode().contains("666"))
                .collect(Collectors.toList());
        List<BulkUploadItem> internalServerError = bulkUploadResponse.getAllList().stream().filter(item -> item.getItemResCode().contains("999"))
                .collect(Collectors.toList());

        // Send Email if there's failure
        if (duplicate.size() != 0 || internalServerError.size() != 0) {

            int totalCount = 0;
            int successCount = 0;
            int totalFailure = 0;
            int duplicateCount = 0;
            int internalErrorCount = 0;
            String companyName = "";
            List<String> adminEmails;


            totalCount = bulkUploadResponse.getAllList().size();
            successCount = success.size();
            totalFailure = duplicate.size() + internalServerError.size();
            duplicateCount = duplicate.size();
            internalErrorCount = internalServerError.size();

            adminEmails = adminManagementRepo.getAdminEmails();
            log.info("Admin Emails {}", adminEmails);

            EservicesSendMailRequest mailRequest = new EservicesSendMailRequest();
            mailRequest.setRecipients(adminEmails);
            mailRequest.setSubject("ISPC NUMBER REPORT BULK UPLOAD ERROR BREAKDOWN");

            mailRequest.setBody("<h3>Some item failed during ISPC Report bulk upload, Please find error breakdown</h3>" +
                    "  <br>" +
                    "  Total uploaded item(s): " + totalCount +
                    "  <br>" +
                    "  Total Successfully uploaded item(s): " + successCount +
                    "  <br>" +
                    "  Total failed item(s): " + totalFailure +
                    "  <br>" +
                    "  Total item that failed with duplicate reason: " + duplicateCount +
                    "  <br>" +
                    "  Total item that with internal server error reason: " + internalErrorCount
            );

            mailRequest.setCc(null);
            mailRequest.setBcc(null);

            try {
                Boolean isMailSent = bulkEmailService.genericMailFunction(mailRequest);
                log.info("Is Mail Sent {}", isMailSent);

                return isMailSent;
            } catch (Exception ex) {
                log.info("Exception occurred while sending mail {}", ex.getMessage());
            }
        }
        return false;
    }

    /**
     * @return
     */
    public GenericResponse<List<ReportIspcNoModel>> getAll() {
        GenericResponse<List<ReportIspcNoModel>> genericResponse = new GenericResponse<>();
        try {
            List<ReportIspcNoModel> createIspcNoModelList = repository.getAll();
            log.info("Result set from repository {} ====> ", createIspcNoModelList);
            if (createIspcNoModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(createIspcNoModelList);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    public GenericResponse<List<ReportIspcNoModel>> getByOrganisation(String organisation) {
        GenericResponse<List<ReportIspcNoModel>> genericResponse = new GenericResponse<>();
        try {
            List<ReportIspcNoModel> createIspcNoModelList = repository.getByOrganisation(organisation);
            log.info("Result set from repository {} ====> ", createIspcNoModelList);
            if (createIspcNoModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(createIspcNoModelList);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

}
