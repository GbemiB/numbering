package com.molcom.nms.number.selectedNumber.service;

import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableCellGetRepository;
import com.molcom.nms.adminmanage.repository.AdminManagementRepo;
import com.molcom.nms.approvals.repository.ApprovalRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
import com.molcom.nms.number.selectedNumber.repository.SelectedNumbersRepo;
import com.molcom.nms.sendBulkEmail.dto.EservicesSendMailRequest;
import com.molcom.nms.sendBulkEmail.service.IBulkEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SelectedNumbersService implements ISelectedNumbersService {

    @Autowired
    private SelectedNumbersRepo repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IBulkEmailService bulkEmailService;

    @Autowired
    private GenericTableCellGetRepository genericTableCellGetRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private AdminManagementRepo adminManagementRepo;

    /**
     * Service implementation to save selected number
     *
     * @param selectedNumberModel
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<SelectedNumberModel> saveSelectedNumber(SelectedNumberModel selectedNumberModel) throws Exception {
        GenericResponse<SelectedNumberModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            // Delete all numbers for the application
            int isCurrentDeleted = repository.deleteByAppId(selectedNumberModel.getApplicationId());
            if (isCurrentDeleted >= 0) {
                responseCode = repository.saveSelectedNumber(selectedNumberModel);
                log.info("AutoFeeResponse code of selected number save ====== {} ", responseCode);
                if (responseCode == 1) {
                    // update available number count
                    updateNumberCount(selectedNumberModel.getNumberSubType(), selectedNumberModel.getSelectedNumberValue());
                    genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                } else {
                    genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                }
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred while saving selected number  {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param list
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<SelectedNumberModel>> saveBulkSelectedNumber(List<SelectedNumberModel> list) throws Exception {
        GenericResponse<List<SelectedNumberModel>> genericResponse = new GenericResponse<>();
        try {
            // get the application id for the first array element
            String appId = "";
            if (list != null) {
                appId = list.get(0).getApplicationId();
            }
            // Delete all numbers for the application
            int isCurrentDeleted = repository.deleteByAppId(appId);
            if (isCurrentDeleted >= 0) {
                SelectedNumberModel selectedNumberModel = new SelectedNumberModel();
                list.forEach(no -> {
                    selectedNumberModel.setSelectedNumberId(no.getSelectedNumberId());
                    selectedNumberModel.setSelectedNumberValue(no.getSelectedNumberValue());
                    selectedNumberModel.setNumberType(no.getNumberType());
                    selectedNumberModel.setNumberSubType(no.getNumberSubType());
                    selectedNumberModel.setApplicationId(no.getApplicationId());
                    selectedNumberModel.setPurpose(no.getPurpose());
                    selectedNumberModel.setBearerMedium(no.getBearerMedium());
                    selectedNumberModel.setCreatedDate(no.getCreatedDate());
                    selectedNumberModel.setTariff(no.getTariff());
                    try {
                        int resCode = repository.saveSelectedNumber(selectedNumberModel);
                        log.info("Save response {}", resCode);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                // update available number count
                updateNumberCount(selectedNumberModel.getNumberSubType(), selectedNumberModel.getSelectedNumberValue());
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while saving selected number  {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to delete selected number
     *
     * @param selectedNumberId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<SelectedNumberModel> deleteSelectedNumber(String selectedNumberId) throws Exception {
        GenericResponse<SelectedNumberModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteSelectedNumber(selectedNumberId);
            log.info("AutoFeeResponse code of selected number  delete ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while deleting selected number  {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to get selected numbers by application id
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<SelectedNumberModel>> getSelectedNumbers(String applicationId) throws Exception {
        GenericResponse<List<SelectedNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<SelectedNumberModel> selectedNumber = repository.getSelectedNumber(applicationId);
            log.info("Find by Application Id: Result set from repository {} ====> ", selectedNumber);

            if (selectedNumber != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(selectedNumber);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred while finding selected number by application id {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param applicationId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<SelectedNumberModel> dropRejectedNumbers(String applicationId) throws Exception {
        GenericResponse<SelectedNumberModel> genericResponse = new GenericResponse<>();
        try {
            List<SelectedNumberModel> selectedNumberModelList = repository.getRejectedNumbers(applicationId);

            if (selectedNumberModelList != null) {
                // send mail to company
                sendMailToForDroppedNumbers(applicationId, selectedNumberModelList);

                // delete the rejected number
                int areRejectedNumbersDeleted = repository.dropRejectedNumbers(applicationId);
                log.info("Reject numbers delete status {} ", areRejectedNumbersDeleted);

                // delete approval step with `REJECTED` as approvalAction
                int isApprovalStepRejected = approvalRepository.deleteRejectedApprovalSteps(applicationId);
                log.info("Is rejected approval step deleted {} ", isApprovalStepRejected);

                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());

            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage("No Rejected Number found for this application");
            }

        } catch (Exception exception) {
            log.info(exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param list
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<SelectedNumberModel>> replaceSelectedNumber(List<SelectedNumberModel> list) throws Exception {
        GenericResponse<List<SelectedNumberModel>> genericResponse = new GenericResponse<>();
        try {
            SelectedNumberModel selectedNumberModel = new SelectedNumberModel();
            list.forEach(no -> {
                selectedNumberModel.setSelectedNumberId(no.getSelectedNumberId());
                selectedNumberModel.setSelectedNumberValue(no.getSelectedNumberValue());
                selectedNumberModel.setNumberType(no.getNumberType());
                selectedNumberModel.setNumberSubType(no.getNumberSubType());
                selectedNumberModel.setApplicationId(no.getApplicationId());
                selectedNumberModel.setPurpose(no.getPurpose());
                selectedNumberModel.setBearerMedium(no.getBearerMedium());
                selectedNumberModel.setCreatedDate(no.getCreatedDate());
                selectedNumberModel.setTariff(no.getTariff());
                try {
                    int resCode = repository.saveSelectedNumber(selectedNumberModel);
                    log.info("Save response {}", resCode);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            String applicationId = list.get(0).getApplicationId();

            // delete the rejected number
            int areRejectedNumbersDeleted = repository.dropRejectedNumbers(applicationId);
            log.info("Reject numbers delete status {} ", areRejectedNumbersDeleted);

            // delete approval step with `REJECTED` as approvalAction
            int isApprovalStepRejected = approvalRepository.deleteRejectedApprovalSteps(applicationId);
            log.info("Is rejected approval step deleted {} ", isApprovalStepRejected);

            // update available number count
            updateNumberCount(selectedNumberModel.getNumberSubType(), selectedNumberModel.getSelectedNumberValue());
            genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
            genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());

        } catch (Exception exception) {
            log.info("Exception occurred while saving selected number  {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
        }
        return genericResponse;
    }


    /**
     * Mail section
     * This method send email to company to inform about numbers to be dropped
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Async
    boolean sendMailToForDroppedNumbers(String applicationId, List<SelectedNumberModel> nos) throws SQLException {
        List<String> numberList = new ArrayList<>();
        String numberString = "";
        String replacementUrl = "";

        nos.forEach(no -> {
            numberList.add(no.getSelectedNumberValue());
        });

        // Convert array of string to comma seperated string
        numberString = String.join(",", numberList);
        log.info("Number  string {} ", numberString);

        // Send mail to user about rejected number
        EservicesSendMailRequest adminMailRequest = new EservicesSendMailRequest();
        EservicesSendMailRequest userMailRequest = new EservicesSendMailRequest();

        String numType = genericTableCellGetRepository.getNumberType(applicationId);
        String numSubType = genericTableCellGetRepository.getNumberSubType(applicationId);
        String companyName = genericTableCellGetRepository.getCompanyNameOfApp(applicationId);
        String companyEmail = genericTableCellGetRepository.getCompanyEmail(applicationId);
        replacementUrl = approvalRepository.getReplacementUrl(applicationId);

        // Send mail to next admin
        List<String> adminEmails = adminManagementRepo.getAdminEmails();
        adminMailRequest.setRecipients(adminEmails); // should be email
        adminMailRequest.setSubject("APPLICATION APPROVAL PROCESS");
        adminMailRequest.setBody("<h3>The following numbers: " + numberString + " were rejected and is dropped for application ID " + applicationId +
                "." +
                "  <br>" +
                " See details below:  <h3/> " +
                "  <br>" +
                " Application ID: " + applicationId +
                "  <br>" +
                " Number Type: " + numType +
                "  <br>" +
                " Number Sub Type: " + numSubType +
                "  <br>");
        adminMailRequest.setCc(null);
        adminMailRequest.setBcc(null);

        // send mail to user
        List<String> companyEmails = new ArrayList<>();
        companyEmails.add(companyEmail);
        userMailRequest.setRecipients(companyEmails); // should be email
        assert companyEmails != null;
        userMailRequest.setSubject("HELLO " + companyName + ",");
        userMailRequest.setBody("<h3>The following numbers: " + numberString + " were rejected and is dropped for application ID " + applicationId +
                "." +
                "  <br>" +
                " See details below:  <h3/> " +
                "  <br>" +
                " Application ID: " + applicationId +
                "  <br>" +
                " Number Type: " + numType +
                "  <br>" +
                " Number Sub Type: " + numSubType + "");
        userMailRequest.setCc(null);
        userMailRequest.setBcc(null);

        try {
            Boolean isMailSentToAdmin = bulkEmailService.genericMailFunction(adminMailRequest);
            Boolean isMailSentToUser = bulkEmailService.genericMailFunction(userMailRequest);
            log.info("Is Mail Sent To Admin {}", isMailSentToAdmin);
            log.info("Is Mail Sent To User{}", isMailSentToUser);

            return isMailSentToAdmin & isMailSentToUser;
        } catch (Exception ex) {
            log.info("Exception occurred while sending mail {}", ex.getMessage());
        }
        return false;
    }

    /**
     * Method update available number count
     *
     * @param numberSubType
     * @param selectedNumber
     */
    public void updateNumberCount(String numberSubType, String selectedNumber) {
        String numSubType = (numberSubType != null ? numberSubType.toUpperCase() : "");
        log.info("Number sub type is {} ", numSubType);


        switch (numSubType) {
            case "NATIONAL":
                try {
                    String numberRange = getNumberRange(selectedNumber);
                    if (numberRange != null) {
                        String[] arrOfStr = numberRange.split("-", 11);
                        String min = arrOfStr[0];
                        String max = arrOfStr[1];
                        log.info("minimumNumber {}", min);
                        log.info("maximumNumber  {}", max);

                        int notSelectedNoCounts = getNotSelectedCount(numberRange);

                        if (min != null && max != null) {
                            int isUpdated = updateAvailableCountStandard(numSubType, notSelectedNoCounts,
                                    min, max);
                            log.info("Update of available number count for national number {} ", isUpdated);
                        }
                    }
                } catch (Exception exception) {
                    log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
                }

            case "GEOGRAPHICAL":
                try {
                    // Number block is not been passed for selected number
                    // But the created number range
                    if (selectedNumber != null) {
                        String[] arrOfStr = selectedNumber.split("-", 11);
                        String min = arrOfStr[0];
                        String max = arrOfStr[1];
                        log.info("minimumNumber {}", min);
                        log.info("maximumNumber  {}", max);
                        if (min != null && max != null) {
                            int isUpdated = updateAvailableCountStandard(numSubType, 0,
                                    min, max);
                            log.info("Update of available number count for geographical {} ", isUpdated);
                        }
                    }
                } catch (Exception exception) {
                    log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
                }
            case "VANITY":
                try {
                    String numberRange = getNumberRange(selectedNumber);
                    if (numberRange != null) {
                        String[] arrOfStr = numberRange.split("-", 11);
                        String min = arrOfStr[0];
                        String max = arrOfStr[1];
                        log.info("minimumNumber {}", min);
                        log.info("maximumNumber  {}", max);

                        int notSelectedNoCounts = getNotSelectedCount(numberRange);

                        if (min != null && max != null) {
                            int isUpdated = updateAvailableCountStandard(numSubType, notSelectedNoCounts,
                                    min, max);
                            log.info("Update of available number count for vanity number {} ", isUpdated);
                        }
                    }
                } catch (Exception exception) {
                    log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
                }
            case "TOLL-FREE":
                try {
                    String numberRange = getNumberRange(selectedNumber);
                    if (numberRange != null) {
                        String[] arrOfStr = numberRange.split("-", 11);
                        String min = arrOfStr[0];
                        String max = arrOfStr[1];
                        log.info("minimumNumber {}", min);
                        log.info("maximumNumber  {}", max);

                        int notSelectedNoCounts = getNotSelectedCount(numberRange);

                        if (min != null && max != null) {
                            int isUpdated = updateAvailableCountStandard(numSubType, notSelectedNoCounts,
                                    min, max);
                            log.info("Update of available number count for toll-free number {} ", isUpdated);
                        }
                    }
                } catch (Exception exception) {
                    log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
                }
            case "SHORT-CODE":
                try {
                    String numberRange = getNumberRange(selectedNumber);
                    if (numberRange != null) {
                        String[] arrOfStr = numberRange.split("-", 11);
                        String min = arrOfStr[0];
                        String max = arrOfStr[1];
                        log.info("minimumNumber {}", min);
                        log.info("maximumNumber  {}", max);

                        int notSelectedNoCounts = getNotSelectedCount(numberRange);

                        if (min != null && max != null) {
                            int isUpdated = updateAvailableCountStandard(numSubType, notSelectedNoCounts,
                                    min, max);
                            log.info("Update of available number count for short code number {} ", isUpdated);
                        }
                    }
                } catch (Exception exception) {
                    log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
                }
            case "ISPC":
                // Ispc doesn't require number block, at the point of save of selected numbers
                // the available count for the number is updated to 0 in IspcNumbertable
            default:
                break;
        }

    }

    /**
     * @param numberRange
     * @return
     */
    public int getNotSelectedCount(String numberRange) {
        int notSelectedCount = 0;
        if (numberRange != null) {
            String sql2 = "SELECT COUNT(*) FROM NumberBlock WHERE NumberRange=? AND IsSelected='FALSE'";
            notSelectedCount = jdbcTemplate.queryForObject(sql2, Integer.class, numberRange);
            log.info("Count numbers not selected {} ", notSelectedCount);
        }
        return notSelectedCount;
    }


    /**
     * @param selectedNumber
     * @return
     */
    public String getNumberRange(String selectedNumber) {
        String sql1 = "SELECT NumberRange FROM NumberBlock WHERE NumberBlock=?";
        String numberRange = jdbcTemplate.queryForObject(
                sql1, new Object[]{selectedNumber}, String.class);
        return numberRange;
    }

    /**
     * @param numberSubType
     * @param count
     * @param min
     * @param max
     * @return
     * @throws SQLException
     */
    public int updateAvailableCountStandard(String numberSubType, int count, String min, String max) throws SQLException {
        if (numberSubType.equalsIgnoreCase("NATIONAL") ||
                numberSubType.equalsIgnoreCase("GEOGRAPHICAL")) {
            String sql = "UPDATE CreatedStandardNumber SET AvailableCount=? WHERE MinimumNumber= ? AND MaximumNumber=?";
            return jdbcTemplate.update(sql,
                    String.valueOf(count),
                    min,
                    max
            );
        }

        if (numberSubType.equalsIgnoreCase("VANITY") ||
                numberSubType.equalsIgnoreCase("TOLL-FREE")) {
            String sql = "UPDATE CreatedSpecialNumber SET AvailableCount=? WHERE MinimumNumber= ? AND MaximumNumber=?";
            return jdbcTemplate.update(sql,
                    String.valueOf(count),
                    min,
                    max
            );
        }

        if (numberSubType.equalsIgnoreCase("SHORT-CODE")) {
            String sql = "UPDATE CreatedShortCodeNumber SET AvailableCount=? WHERE MinimumNumber= ? AND MaximumNumber=?";
            return jdbcTemplate.update(sql,
                    String.valueOf(count),
                    min,
                    max
            );

        }
        return -1;
    }
}
