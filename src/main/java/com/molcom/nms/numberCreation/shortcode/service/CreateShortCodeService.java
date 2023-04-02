package com.molcom.nms.numberCreation.shortcode.service;

import com.molcom.nms.adminmanage.repository.IAdminManagementRepo;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.numberCreation.shortcode.dto.*;
import com.molcom.nms.numberCreation.shortcode.repository.CreateShortCodeRepository;
import com.molcom.nms.sendBulkEmail.dto.EservicesSendMailRequest;
import com.molcom.nms.sendBulkEmail.service.IBulkEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class CreateShortCodeService {
    @Autowired
    private CreateShortCodeRepository repository;

    @Autowired
    private IBulkEmailService bulkEmailService;

    @Autowired
    private IAdminManagementRepo adminManagementRepo;

    public GenericResponse<CreateShortCodeNoModel> createSingleNumber(CreateShortCodeNoModel model) {
        log.info("RequestBody", model);
        GenericResponse<CreateShortCodeNoModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            String containsErrors = validateShortCodeNumber(model.getMinimumNumber(), model.getMaximumNumber(), model.getShortCodeService(), model.getShortCodeCategory());
            log.info("Error {}", containsErrors);
            // If number is not valid, return error message
            if (!Objects.equals(containsErrors, "NO-ERRORS")) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage(containsErrors);
            } else {
                // save short number range
                responseCode = repository.createSingleNumber(model);
                log.info("AutoFeeResponse code for saving new area code {} ", responseCode);
                if (responseCode == 1) {
                    // convert min and max number to integer and extra all numbers in the range
                    int min = Integer.parseInt(model.getMinimumNumber());
                    int max = Integer.parseInt(model.getMaximumNumber());
                    List<Integer> numberListing = IntStream.range(min, max).boxed().collect(Collectors.toList());
                    log.info("Number listing {} ", numberListing);
                    // Add last number i.e lower band
                    numberListing.add(max);
                    log.info("Number listing Updated {} ", numberListing);
                    numberListing.forEach(numberList -> {
                        // convert the number back to string for persist of number block of short code number
                        String numberBlock = String.valueOf(numberList);
                        try {
                            repository.saveNumberBlock("Short-code",
                                    model.getShortCodeService(), // short code category as number subtype
                                    model.getMinimumNumber() + "-" + model.getMaximumNumber(),
                                    numberBlock
                            );
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                    genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                } else {
                    genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                }
            }
        } catch (Exception exception) {
            log.info("Exception occurred  ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    private String validateShortCodeNumber(String minNumber, String maxNumber, String service, String category) throws SQLException {
        String errorMessage = "";
        log.info("short code service {}", service);
        int count = repository.countIfNoExist(minNumber, maxNumber, service);
        int isLengthValidate = repository.validateLength(minNumber, maxNumber, category);
        if (count >= 1) {
            log.info("count {}", count);
            errorMessage = "Number block either already exists or encroaches on an existing block!";
        } else if (isLengthValidate == -1) {
            errorMessage = "Short category selected doesn't match with length provided";
        } else if (Integer.parseInt(minNumber) > Integer.parseInt(maxNumber)) {
            errorMessage = " Minimum number must not be greater than maximum number";
        } else {
            errorMessage = "NO-ERRORS";
        }
        return errorMessage;
    }

    public BulkUploadResponse bulkShortCodeInsert(BulkUploadRequestShortCode bulkUploadRequest) throws Exception {
        BulkUploadResponse bulkUploadResponse = new BulkUploadResponse();
        List<BulkUploadItem> bulkUploadItem = new ArrayList<>();
        bulkUploadRequest.getBulkList().forEach(bulkItem -> {
            CreateShortCodeNoModel model = new CreateShortCodeNoModel();
            BulkUploadItem item = new BulkUploadItem();
            model.setNumberType("Short-Code");
            model.setShortCodeCategory(bulkItem.getShortCodeCategory());
            model.setShortCodeService(bulkItem.getShortCodeService());
            model.setMinimumNumber(bulkItem.getMinimumNumber());
            model.setMaximumNumber(bulkItem.getMaximumNumber());
            model.setCreatedBy(bulkUploadRequest.getBatchId());
            model.setCreatedDate(bulkItem.getCreatedDate());
            try {
                String containsErrors = validateShortCodeNumber(model.getMinimumNumber(), model.getMaximumNumber(), model.getShortCodeService(), model.getShortCodeCategory());
                log.info("Error {}", containsErrors);
                // If number is not valid, return error message
                if (!Objects.equals(containsErrors, "NO-ERRORS")) {
                    item.setItemResCode(ResponseStatus.ALREADY_EXIST.getCode());
                    item.setItemResMessage(containsErrors);
                } else {
                    // save short number range
                    int responseCode = repository.createSingleNumber(model);
                    log.info("AutoFeeResponse code for insert of {} ", bulkItem.getBulkUploadId());
                    // Checking if the insert was successful for the individual bulk upload
                    if (Objects.equals(responseCode, 1)) {
                        item.setItemId(bulkItem.getMinimumNumber() + "-" + bulkItem.getMaximumNumber());
                        item.setItemResCode("000");
                        item.setItemResMessage("SUCCESS");
                    } else {
                        // Checking if the insert failed for the individual bulk upload
                        item.setItemId(bulkItem.getMinimumNumber() + "-" + bulkItem.getMaximumNumber());
                        item.setItemResCode("999");
                        item.setItemResMessage("Error occurred, try later");
                    }
                }
                // Adding response code for insert of each item in the list
                bulkUploadItem.add(item);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        bulkUploadResponse.setAllList(bulkUploadItem);
        bulkUploadResponse.setBatchId(RefGenerator.getRefNo(5)); //Batch Id
        bulkUploadResponse.setTotalCount(String.valueOf(bulkUploadRequest.getBulkList().size()));

        sendMailIfBulkUploadFails(bulkUploadResponse, bulkUploadRequest);

        return bulkUploadResponse;
    }

    // Mail section
    @Async
    boolean sendMailIfBulkUploadFails(BulkUploadResponse bulkUploadResponse, BulkUploadRequestShortCode bulkUploadRequest) {
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

            // pick created user from the 1st element in array
//            String createdUser = bulkUploadRequest.getBulkList().get(0).getCreatedBy();
//            log.info("Created User:  {}", createdUser);
//            companyName = adminManagementRepo.getEmailOrganisation(createdUser);
//            log.info("Company name {}", createdUser);
//            adminEmails = adminManagementRepo.getEmailOfFellowAdmin(createdUser);
//            log.info("Admin Emails {}", adminEmails);

            totalCount = bulkUploadResponse.getAllList().size();
            successCount = success.size();
            totalFailure = duplicate.size() + internalServerError.size();
            duplicateCount = duplicate.size();
            internalErrorCount = internalServerError.size();

            adminEmails = adminManagementRepo.getAdminEmails();
            log.info("Admin Emails {}", adminEmails);

            EservicesSendMailRequest mailRequest = new EservicesSendMailRequest();
            mailRequest.setRecipients(adminEmails);
            mailRequest.setSubject("SHORT CODE NUMBER CREATION BULK UPLOAD ERROR BREAKDOWN");

            mailRequest.setBody("<h3>Some item failed during Short Code Number Creation bulk upload, Please find error breakdown</h3>" +
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

    public GenericResponse<BulkUploadResponse> createBulkNumber(BulkUploadRequestShortCode bulkUploadRequest) {
        GenericResponse<BulkUploadResponse> genericResponse = new GenericResponse<>();
        try {
            BulkUploadResponse response = bulkShortCodeInsert(bulkUploadRequest);
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
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!!!! ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    public GenericResponse<CreateShortCodeNoModel> deleteNumberById(String id) {
        GenericResponse<CreateShortCodeNoModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteNumberById(id);
            log.info("AutoFeeResponse code ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    public GenericResponse<List<CreateShortCodeNoModel>> getNumberByService(String serviceName) {
        GenericResponse<List<CreateShortCodeNoModel>> genericResponse = new GenericResponse<>();
        try {
            List<CreateShortCodeNoModel> response = repository.getNumberByService(serviceName);
            log.info("AutoFeeResponse payload ====== {} ", response);
            if (response != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(response);
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    public GenericResponse<List<CreateShortCodeNoModel>> getAllNumber() {
        GenericResponse<List<CreateShortCodeNoModel>> genericResponse = new GenericResponse<>();
        try {
            List<CreateShortCodeNoModel> CreateSpecialNoModelList = repository.getAllNumber();
            log.info("Result set from repository {} ====> ", CreateSpecialNoModelList);
            if (CreateSpecialNoModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(CreateSpecialNoModelList);
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

    public GenericResponse<List<CreateShortCodeNoModel>> filterNumber(String category, String service, String startDate, String endDate, String rowNumber) {
        GenericResponse<List<CreateShortCodeNoModel>> genericResponse = new GenericResponse<>();
        try {
            List<CreateShortCodeNoModel> CreateSpecialNoModelList = repository.filterNumber(category, service, startDate, endDate, rowNumber);
            log.info("Result set from repository {} ====> ", CreateSpecialNoModelList);
            if (CreateSpecialNoModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(CreateSpecialNoModelList);
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

    public GenericResponse<BulkUploadResponse> uploadExistingBulkUpload(BulkUploadExistingShortCode bulkUploadRequest) {
        GenericResponse<BulkUploadResponse> genericResponse = new GenericResponse<>();
        try {
            BulkUploadResponse response = repository.uploadExistingShortCode(bulkUploadRequest);
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
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!! ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

}
