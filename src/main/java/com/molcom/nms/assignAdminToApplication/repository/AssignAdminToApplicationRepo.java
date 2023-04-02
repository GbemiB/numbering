package com.molcom.nms.assignAdminToApplication.repository;

import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableCellGetRepository;
import com.molcom.nms.GenericDatabaseUpdates.repository.IGenericTableUpdateRepository;
import com.molcom.nms.adminmanage.repository.IAdminManagementRepo;
import com.molcom.nms.approvals.repository.ApprovalRepository;
import com.molcom.nms.assignAdminToApplication.dto.*;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.sendBulkEmail.dto.EservicesSendMailRequest;
import com.molcom.nms.sendBulkEmail.service.IBulkEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class AssignAdminToApplicationRepo {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private IBulkEmailService bulkEmailService;

    @Autowired
    private ApprovalRepository repository;

    @Autowired
    private IAdminManagementRepo adminManagementRepo;

    @Autowired
    private GenericTableCellGetRepository genericTableCellGetRepository;

    @Autowired
    private IGenericTableUpdateRepository genericTableUpdateRepository;


    /**
     * Data layer to assign admin to application
     *
     * @param applicationId
     * @param workFlowStepName
     * @return
     * @throws SQLException
     */
    public int alreadyAssigned(String applicationId, String workFlowStepName) throws SQLException {
        String sql = "SELECT count(*) FROM ApplicationApprovalTracking WHERE ApplicationId=? AND WorkFlowStepName=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, applicationId, workFlowStepName);
    }

    /**
     * Data layer to save admin to application
     *
     * @param model
     * @return
     * @throws SQLException
     */
    public int save(AssignAdminToAppRemap model) throws SQLException {
        String sql = "INSERT INTO ApplicationApprovalTracking (Process, ApplicationId, WorkFlowStepNum, WorkFlowStepName, GenerateInvoice, AssignedUserName, AssignedUserEmail,AssignedDate) VALUES(?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                model.getProcess(),
                model.getApplicationId(),
                model.getWorkFlowStepNum(),
                model.getWorkFlowStepName(),
                model.getGenerateInvoice(),
                model.getAssignedUserName(),
                model.getAssignedUserEmail(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }


    /**
     * Data layer for bulk insert
     *
     * @param bulkUploadRequest
     * @return
     * @throws SQLException
     */
    public AssignAppToAdminBlkRes bulkInsert(AssignAppToAdminBlkStep bulkUploadRequest) throws SQLException {
        log.info("Bulk insert");
        log.info("AssignAppToAdminBlkStep {}", bulkUploadRequest);
        AssignAppToAdminBlkRes bulkUploadResponse = new AssignAppToAdminBlkRes();
        List<AssignAppToAdminItem> bulkUploadItem = new ArrayList<>();

        bulkUploadRequest.getAssignApplication().forEach(bulkItem -> {
            AssignAdminToAppRemap model = new AssignAdminToAppRemap();
            AssignAppToAdminItem item = new AssignAppToAdminItem();

            // convert array of string to comma seperated string
            String usernameComma = String.join(",", bulkItem.getAssignedUserName());
            String emailComma = String.join(",", bulkItem.getAssignedUserEmail());

            // build data
            model.setApplicationId(bulkItem.getApplicationId());
            model.setProcess(bulkItem.getProcess());
            model.setAssignedUserName(usernameComma);
            model.setAssignedUserEmail(emailComma);
            model.setGenerateInvoice(bulkItem.getGenerateInvoice());
            model.setWorkFlowStepNum(bulkItem.getWorkFlowStepNum());
            model.setWorkFlowStepName(bulkItem.getWorkFlowStepName());
            try {
                int count = alreadyAssigned(model.getApplicationId(), model.getWorkFlowStepName());
                log.info("Checking bulk count {} ", count);
                if (count >= 1) {
                    item.setItemResCode(ResponseStatus.ALREADY_EXIST.getCode());
                    item.setItemResMessage("ALREADY ASSIGNED");
                } else {
                    int responseCode = save(model);
                    if (Objects.equals(responseCode, 1)) {
                        // send mail to admin
                        EservicesSendMailRequest adminMailRequest = new EservicesSendMailRequest();
                        EservicesSendMailRequest userMailRequest = new EservicesSendMailRequest();

                        // Get first approval flow
                        List<AssignAdminToAppModel> approveApplicationModelList = repository.getFirstApprovalProcess(
                                model.getApplicationId(),
                                "1");

                        log.info("First approval object {} ", approveApplicationModelList);
                        if (approveApplicationModelList != null && approveApplicationModelList.size() >= 1) {
                            AssignAdminToAppModel nextApprovalFlow = approveApplicationModelList.get(0);

                            log.info("Next approval object {} ", approveApplicationModelList);

                            // Get mail to first admin and to user that approval has started
                            if (nextApprovalFlow != null && nextApprovalFlow.getAssignedUserEmail() != null) {
                                String appId = nextApprovalFlow.getApplicationId();
                                String numType = genericTableCellGetRepository.getNumberType(appId);
                                String numSubType = genericTableCellGetRepository.getNumberSubType(appId);
                                String companyName = genericTableCellGetRepository.getCompanyNameOfApp(appId);
                                String companyEmail = genericTableCellGetRepository.getCompanyEmail(appId);

                                // Send mail to next admin
                                List<String> adminEmails = nextApprovalFlow.getAssignedUserEmail();
                                adminMailRequest.setRecipients(adminEmails); // should be email
                                adminMailRequest.setSubject("APPLICATION APPROVAL PROCESS");
                                adminMailRequest.setBody("<h3>A new application require your attention, See details below:  <h3/> " +
                                        "  <br>" +
                                        " Application ID: " + appId +
                                        "  <br>" +
                                        " Number Type: " + numType +
                                        "  <br>" +
                                        " Number Sub Type: " + numSubType +
                                        "  <br>" +
                                        " login to take action");
                                adminMailRequest.setCc(null);
                                adminMailRequest.setBcc(null);

                                // send mail to user
                                List<String> companyEmails = new ArrayList<>();
                                assert companyEmails != null;
                                companyEmails.add(companyEmail);
                                String company = (companyName != null ? companyName.toUpperCase() : "");
                                userMailRequest.setRecipients(companyEmails); // should be email
                                userMailRequest.setSubject("HELLO " + company + ",");
                                userMailRequest.setBody("<h3>Congratulations, approval process has started for your application. See details below: <h3/>  " +
                                        "  <br>" +
                                        " Application ID: " + appId +
                                        "  <br>" +
                                        " Number Type: " + numType +
                                        "  <br>" +
                                        " Number Sub Type: " + numSubType +
                                        "  <br>" +
                                        " login to take action");
                                userMailRequest.setCc(null);
                                userMailRequest.setBcc(null);

                                try {
                                    Boolean isMailSentToAdmin = bulkEmailService.genericMailFunction(adminMailRequest);
                                    Boolean isMailSentToUser = bulkEmailService.genericMailFunction(userMailRequest);
                                    log.info("Is Mail Sent To Admin {}", isMailSentToAdmin);
                                    log.info("Is Mail Sent To User{}", isMailSentToUser);
                                } catch (Exception ex) {
                                    log.info("Exception occurred while sending mail {}", ex.getMessage());
                                }
                            }
                        }
                        item.setItemId(model.getWorkFlowStepName());
                        item.setItemResCode("000");
                        item.setItemResMessage("SUCCESS");

                        // update `isApplicationAssigned` to inform that application is assigned ;
                        String numberSubType = genericTableCellGetRepository.getNumberSubType(model.getApplicationId());
                        updateIsApplicationAssigned(model.getApplicationId(), numberSubType);

                    } else {
                        item.setItemResCode("999");
                        item.setItemResMessage("FAILED TO INSERT RECORD TO");
                    }
                }
                bulkUploadItem.add(item);

            } catch (SQLException e) {
                log.info("Exception ", e.getMessage());
                e.printStackTrace();
            }
        });
        bulkUploadResponse.setAllList(bulkUploadItem);
        bulkUploadResponse.setBatchId(RefGenerator.getRefNo(5)); //Batch Id
        bulkUploadResponse.setTotalCount(String.valueOf(bulkUploadRequest.getAssignApplication().size()));
        return bulkUploadResponse;
    }

    /**
     * Data layer to delete by id
     *
     * @param assignAdminToAppId
     * @return
     * @throws SQLException
     */
    public int deleteById(int assignAdminToAppId) throws SQLException {
        String sql = "DELETE FROM ApplicationApprovalTracking WHERE WorkFlowTrackingId = ?";
        return jdbcTemplate.update(sql, assignAdminToAppId);
    }


    /**
     * Data layer to get by id
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    public List<AssignAdminToAppModel> getById(String applicationId) throws SQLException {
        String sql = "SELECT * FROM ApplicationApprovalTracking WHERE ApplicationId = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(AssignAdminToAppModel.class), applicationId);
    }

    /**
     * Data layer to update assignment
     *
     * @param applicationId
     * @param numberType
     * @return
     * @throws SQLException
     */
    public boolean updateIsApplicationAssigned(String applicationId, String numberType) throws SQLException {
        int updateIsApplicationAssigned;
        String tableName = "";
        String columnConstraintKey = "ApplicationId";
        String numType = (numberType != null ? numberType.toUpperCase() : "");

        // if the application is present in addition of services
        // update is `IsApplicationAssigned` flag to true
        String additionOfServiceTable = "AdditionOfService";
        updateIsApplicationAssigned = genericTableUpdateRepository.updateTableColumn(
                additionOfServiceTable,
                "IsApplicationAssigned",
                "TRUE",
                columnConstraintKey,
                applicationId);
        log.info("Is addition of services assigned response code {}", updateIsApplicationAssigned);

        try {
            switch (numType) {
                case "NATIONAL":
                case "GEOGRAPHICAL":
                    tableName = "StandardNumber";
                    break;

                case "ISPC":
                    tableName = "IspcNumbers";
                    break;

                case "VANITY":
                case "TOLL-FREE":
                    tableName = "SpecialNumbers";
                    break;

                case "SHORT-CODE":
                    tableName = "ShortCodeNumbers";

                default:
                    break;
            }

            updateIsApplicationAssigned = genericTableUpdateRepository.updateTableColumn(tableName,
                    "IsApplicationAssigned",
                    "TRUE",
                    columnConstraintKey,
                    applicationId);
            log.info("Is application assigned response code {}", updateIsApplicationAssigned);

            return updateIsApplicationAssigned >= 1;

        } catch (Exception exe) {
            log.info("Exception occurred during admin assignment {} ", exe.getMessage());
            return false;
        }

    }

}
