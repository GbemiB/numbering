package com.molcom.nms.numberCreation.standard.service;

import com.molcom.nms.adminmanage.repository.IAdminManagementRepo;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.numberCreation.standard.dto.BulkUploadItem;
import com.molcom.nms.numberCreation.standard.dto.BulkUploadRequestStandard;
import com.molcom.nms.numberCreation.standard.dto.BulkUploadResponse;
import com.molcom.nms.numberCreation.standard.dto.CreateStandardNoModel;
import com.molcom.nms.numberCreation.standard.repository.CreateStandardNoRepository;
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

@Service
@Slf4j
public class CreateStandardNoService {
    @Autowired
    private CreateStandardNoRepository repository;

    @Autowired
    private IBulkEmailService bulkEmailService;

    @Autowired
    private IAdminManagementRepo adminManagementRepo;

    public GenericResponse<CreateStandardNoModel> createSingleNumber(CreateStandardNoModel model) {
        GenericResponse<CreateStandardNoModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            String containsErrors = validateStandardNumber(model.getAccessCode(), model.getMinimumNumber(), model.getMaximumNumber(), model.getNumberSubType());
            log.info("Error {}", containsErrors);
            // If number is not valid, return error message
            if (!Objects.equals(containsErrors, "NO-ERRORS")) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage(containsErrors);
            } else {
                // If number is valid, save number range
                responseCode = repository.createSingleNumber(model);

                // Number block for national number
                if (model.getNumberSubType().equalsIgnoreCase("National")) {
                    if (responseCode == 1) {
                        // If number range is saved, get all numbers in the number range
                        List<String> numberListing = new ArrayList<>();
                        int minLength = model.getMinimumNumber().length();
                        int maxLength = model.getMaximumNumber().length();

                        // create number based on the format of standard number and persist in number block table
                        numberListing.add(model.getAccessCode() + model.getMinimumNumber() + "-" + model.getAccessCode() + "0" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "1" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "1" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "2" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "2" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "3" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "3" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "4" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "4" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "5" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "5" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "6" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "6" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "7" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "7" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "8" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "8" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "9" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "9" + model.getMaximumNumber().substring(0, maxLength - 1));

                        log.info("Number listing {} ", numberListing);

                        numberListing.forEach(numberList -> {
                            // convert the number back to string for persist of number block of short code number
                            try {
                                repository.saveNumberBlock("Standard",
                                        model.getNumberSubType(),
                                        model.getAccessCode() + model.getMinimumNumber() + "-" + model.getAccessCode() + model.getMaximumNumber(),
                                        numberList
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

                // No number block for geographical number
                if (model.getNumberSubType().equalsIgnoreCase("Geographical")) {
                    if (responseCode == 1) {
                        repository.saveNumberBlock("Standard",
                                model.getNumberSubType(),
                                model.getMinimumNumber() + "-" + model.getMaximumNumber(),
                                model.getMinimumNumber() + "-" + model.getMaximumNumber());

                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                    } else {
                        genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                    }

                }


            }
        } catch (Exception exception) {
            log.info("Exception occurred when creating standard number {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    private String validateStandardNumber(String accessCode, String minNumber, String maxNumber, String numberSubType) throws SQLException {
        String errorMessage = "";
        String subType = (numberSubType != null ? numberSubType.toUpperCase() : "");
        int count = repository.countIfNoExist(minNumber, maxNumber, numberSubType, accessCode);
        log.info("count {}", count);
        Integer val = calculateBtwRange(minNumber, maxNumber);
        log.info("number of lines {}", val);

        if (count >= 1) {
            errorMessage = "Number block either already exists or encroaches on an existing block!";
        } else if (Integer.parseInt(minNumber) > Integer.parseInt(maxNumber)) {
            errorMessage = numberSubType + " Minimum number must not be greater than maximum number";
        } else if (Integer.parseInt(minNumber) % 10 != 0 && Integer.parseInt(maxNumber) % 9 != 0) {
            errorMessage = numberSubType + " Range between minimum and maximum must not be less than 1000000";
        } else if ((Objects.equals(subType, "NATIONAL")) && (accessCode.length() + minNumber.length() != 11)) {
            errorMessage = " Length of access code and minimum or maximum " +
                    "must not be greater or less that 11";
        } else if ((Objects.equals(subType, "NATIONAL")) && (accessCode.length() + maxNumber.length() != 11)) {
            errorMessage = " Length of access code and minimum or maximum " +
                    "must not be greater or less that 11";
        } else if ((Objects.equals(subType, "GEOGRAPHICAL")) && (!Objects.equals(val, 10000))) {
            errorMessage = " Total lines of geographical number should be 10,000";
        } else {
            errorMessage = "NO-ERRORS";
        }
        return errorMessage;
    }

    private Integer calculateBtwRange(String minNumber, String maxNumber) {
        Integer min = Integer.parseInt(maxNumber);
        Integer max = Integer.parseInt(minNumber);
        Integer numberRange = min - max + 1;
        return numberRange;
    }


    public GenericResponse<BulkUploadResponse> createBulkNumber(BulkUploadRequestStandard bulkUploadRequest) {
        GenericResponse<BulkUploadResponse> genericResponse = new GenericResponse<>();
        try {
            BulkUploadResponse response = bulkinsert(bulkUploadRequest);
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

    private BulkUploadResponse bulkinsert(BulkUploadRequestStandard bulkUploadRequest) throws SQLException {
        log.info("Bulk insert");
        BulkUploadResponse bulkUploadResponse = new BulkUploadResponse();
        List<BulkUploadItem> bulkUploadItem = new ArrayList<>();
        bulkUploadRequest.getBulkList().forEach(bulkItem -> {
            CreateStandardNoModel model = new CreateStandardNoModel();
            BulkUploadItem item = new BulkUploadItem();
            model.setNumberType("Standard");
            model.setNumberSubType(bulkItem.getNumberSubType());
            model.setCoverageArea(bulkItem.getCoverageArea());
            model.setAccessCode(bulkItem.getAccessCode());
            model.setMinimumNumber(bulkItem.getMinimumNumber());
            model.setMaximumNumber(bulkItem.getMaximumNumber());
            model.setCreatedBy(bulkUploadRequest.getBatchId());
            model.setCreatedDate(bulkItem.getCreatedDate());
            try {

                String containsErrors = validateStandardNumber(model.getAccessCode(), model.getMinimumNumber(), model.getMaximumNumber(), model.getNumberSubType());
                log.info("Error {}", containsErrors);
                // If number is not valid, return error message
                if (!Objects.equals(containsErrors, "NO-ERRORS")) {
                    item.setItemResCode(ResponseStatus.ALREADY_EXIST.getCode());
                    item.setItemResMessage(containsErrors);
                } else {
                    int responseCode = repository.createSingleNumber(model);
                    // Checking if the insert was successful for the individual bulk upload
                    if (Objects.equals(responseCode, 1)) {

                        // If number range is saved, get all numbers in the number range
                        List<String> numberListing = new ArrayList<>();
                        int minLength = model.getMinimumNumber().length();
                        int maxLength = model.getMaximumNumber().length();

                        // create number based on the format of standard number and persist in number block table
                        numberListing.add(model.getAccessCode() + model.getMinimumNumber() + "-" + model.getAccessCode() + "0" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "1" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "1" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "2" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "2" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "3" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "3" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "4" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "4" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "5" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "5" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "6" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "6" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "7" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "7" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "8" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "8" + model.getMaximumNumber().substring(0, maxLength - 1));
                        numberListing.add(model.getAccessCode() + "9" + model.getMinimumNumber().substring(0, minLength - 1) + "-" + model.getAccessCode() + "9" + model.getMaximumNumber().substring(0, maxLength - 1));

                        log.info("Number listing {} ", numberListing);

                        numberListing.forEach(numberList -> {
                            // convert the number back to string for persist of number block of short code number
                            try {
                                repository.saveNumberBlock("Standard",
                                        model.getNumberSubType(),
                                        model.getAccessCode() + model.getMinimumNumber() + "-" + model.getAccessCode() + model.getMaximumNumber(),
                                        numberList
                                );
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
                        item.setItemId(model.getMinimumNumber() + "-" + model.getMaximumNumber());
                        item.setItemResCode("000");
                        item.setItemResMessage("SUCCESS");
                    } else {
                        // Checking if the insert failed for the individual bulk upload
                        item.setItemId(model.getMinimumNumber() + "-" + model.getMaximumNumber());
                        item.setItemResCode("999");
                        item.setItemResMessage("Error occurred, try later");
                    }
                }
                //    Adding response code for insert of each item in the list
                bulkUploadItem.add(item);
            } catch (Exception e) {
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
    boolean sendMailIfBulkUploadFails(BulkUploadResponse bulkUploadResponse, BulkUploadRequestStandard bulkUploadRequest) {
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
            mailRequest.setSubject("STANDARD NUMBER CREATION BULK UPLOAD ERROR BREAKDOWN");

            mailRequest.setBody("<h3>Some item failed during Standard Number Creation bulk upload, Please find error breakdown</h3>" +
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

    public GenericResponse<CreateStandardNoModel> deleteNumberById(String id) {
        GenericResponse<CreateStandardNoModel> genericResponse = new GenericResponse<>();
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


    public GenericResponse<List<CreateStandardNoModel>> getNumberByAccessCode(String accessCode) {
        GenericResponse<List<CreateStandardNoModel>> genericResponse = new GenericResponse<>();
        try {
            List<CreateStandardNoModel> response = repository.getNumberByAccessCode(accessCode);
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

    public GenericResponse<List<CreateStandardNoModel>> getAllNumber() {
        GenericResponse<List<CreateStandardNoModel>> genericResponse = new GenericResponse<>();
        try {
            List<CreateStandardNoModel> CreateStandardNoModelList = repository.getAllNumber();
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

    public GenericResponse<List<CreateStandardNoModel>> filterNumber(String numberSubType, String coverageArea, String areaCode, String accessCode, String startDate, String endDate, String rowNumber) {
        GenericResponse<List<CreateStandardNoModel>> genericResponse = new GenericResponse<>();
        try {
            List<CreateStandardNoModel> CreateStandardNoModelList = repository.filterNumber(numberSubType, coverageArea, areaCode, accessCode, startDate, endDate, rowNumber);
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
}
