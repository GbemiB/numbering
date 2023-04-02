package com.molcom.nms.numberReport.standard.service;

import com.molcom.nms.adminmanage.repository.IAdminManagementRepo;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.numberReport.standard.dto.BulkUploadItem;
import com.molcom.nms.numberReport.standard.dto.BulkUploadReportStandard;
import com.molcom.nms.numberReport.standard.dto.BulkUploadResponse;
import com.molcom.nms.numberReport.standard.dto.ReportStandardNoModel;
import com.molcom.nms.numberReport.standard.repository.ReportStandardNoRepository;
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
public class ReportStandardNoService {
    @Autowired
    private ReportStandardNoRepository repository;

    @Autowired
    private IBulkEmailService bulkEmailService;

    @Autowired
    private IAdminManagementRepo adminManagementRepo;

    /**
     * @param model
     * @return
     */
    public GenericResponse<ReportStandardNoModel> createSingleNumber(ReportStandardNoModel model) {
        log.info("RequestBody", model);
        GenericResponse<ReportStandardNoModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            int count = repository.countIfNoExist(model.getMinimumNumber(), model.getMaximumNumber(), model.getNumberSubType(), model.getApplicationId());
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
     * @param bulkUploadRequest
     * @return
     */
    public GenericResponse<BulkUploadResponse> createBulkNumber(BulkUploadReportStandard bulkUploadRequest) {
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
    boolean sendMailIfBulkUploadFails(BulkUploadResponse bulkUploadResponse, BulkUploadReportStandard bulkUploadRequest) {
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

            mailRequest.setBody("<h3>Some item failed during ISPC Number Report bulk upload, Please find error breakdown</h3>" +
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
     * @param organisation
     * @param numberSubType
     * @param coverageArea
     * @param areaCode
     * @param accessCode
     * @param startDate
     * @param endDate
     * @param rowNumber
     * @return
     */
    public GenericResponse<List<ReportStandardNoModel>> filterNumber(String organisation, String numberSubType, String coverageArea, String areaCode, String accessCode, String startDate, String endDate, String rowNumber) {
        GenericResponse<List<ReportStandardNoModel>> genericResponse = new GenericResponse<>();
        try {
            List<ReportStandardNoModel> CreateStandardNoModelList = repository.filterNumber(organisation, numberSubType, coverageArea, areaCode, accessCode, startDate, endDate, rowNumber);
            log.info("Result set from repository {} ====> ", CreateStandardNoModelList);
            if (CreateStandardNoModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(CreateStandardNoModelList);
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
     * @return
     */
    public GenericResponse<List<ReportStandardNoModel>> getAll() {
        GenericResponse<List<ReportStandardNoModel>> genericResponse = new GenericResponse<>();
        try {
            List<ReportStandardNoModel> standardNoModels = repository.getAll();
            log.info("Result set from repository {} ====> ", standardNoModels);
            if (standardNoModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(standardNoModels);
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


    public GenericResponse<List<ReportStandardNoModel>> getByOrganisation(String organisation) {
        GenericResponse<List<ReportStandardNoModel>> genericResponse = new GenericResponse<>();
        try {
            List<ReportStandardNoModel> standardNoModels = repository.getByOrganisation(organisation);
            log.info("Result set from repository {} ====> ", standardNoModels);
            if (standardNoModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(standardNoModels);
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
