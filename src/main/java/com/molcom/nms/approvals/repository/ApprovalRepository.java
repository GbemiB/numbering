package com.molcom.nms.approvals.repository;

import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableUpdateRepository;
import com.molcom.nms.approvals.dto.ApprovalSupportingDocument;
import com.molcom.nms.approvals.dto.ApproveApplicationModel;
import com.molcom.nms.approvals.dto.ApproveApplicationObject;
import com.molcom.nms.assignAdminToApplication.dto.AssignAdminToAppModel;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class ApprovalRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GenericTableUpdateRepository genericTableUpdateRepository;

    /**
     * Method to get approval step
     *
     * @param workflowStepNum
     * @return
     */
    public String getNextStep(String workflowStepNum) {
        log.info("Current step in get next step method{}", workflowStepNum);
        String nextStep = "";

        if (Objects.equals(workflowStepNum, "1")) {
            nextStep = "2";
        } else if (Objects.equals(workflowStepNum, "2")) {
            nextStep = "3";
        } else if (Objects.equals(workflowStepNum, "3")) {
            nextStep = "4";
        } else if (Objects.equals(workflowStepNum, "4")) {
            nextStep = "5";
        } else if (Objects.equals(workflowStepNum, "5")) {
            nextStep = "6";
        } else if (Objects.equals(workflowStepNum, "6")) {
            nextStep = "7";
        } else if (Objects.equals(workflowStepNum, "7")) {
            nextStep = "8";
        } else if (Objects.equals(workflowStepNum, "8")) {
            nextStep = "9";
        } else if (Objects.equals(workflowStepNum, "9")) {
            nextStep = "10";
        } else {
            nextStep = "";
        }
        return nextStep;
    }


    /**
     * Method to get next approval process for an application
     *
     * @param applicationId
     * @param process
     * @param workFlowStepNum
     * @return
     * @throws SQLException
     */
    public List<AssignAdminToAppModel> getNextApprovalProcess(String applicationId,
                                                              String process,
                                                              String workFlowStepNum) throws SQLException {
        String sql2 = "SELECT * from ApplicationApprovalTracking WHERE ApplicationId=? AND Process=? AND WorkFlowStepNum=?";
        List<AssignAdminToAppModel> approvalProcess = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(AssignAdminToAppModel.class), applicationId, process, workFlowStepNum);
        log.info("ApprovalProcess {} ", approvalProcess);
        return approvalProcess;
    }

    /**
     * Method to get first approval process for an application
     *
     * @param applicationId
     * @param workFlowStepNum
     * @return
     * @throws SQLException
     */
    public List<AssignAdminToAppModel> getFirstApprovalProcess(String applicationId,
                                                               String workFlowStepNum) throws SQLException {
        String sql2 = "SELECT * from ApplicationApprovalTracking WHERE ApplicationId=? AND WorkFlowStepNum=?";
        List<AssignAdminToAppModel> approvalProcess = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(AssignAdminToAppModel.class), applicationId, workFlowStepNum);
        log.info("ApprovalProcess {} ", approvalProcess);
        return approvalProcess;
    }

    /**
     * Method to approve application step
     *
     * @param model
     * @return
     * @throws SQLException
     */

    public int approveApplicationStep(ApproveApplicationModel model) throws SQLException {
        String rejectedString = "";
        log.info("Supporting document size {}", model.getSupportingDocument().size());
        if (model.getSupportingDocument().size() > 0) {
            model.getSupportingDocument().forEach(docu -> {
                ApprovalSupportingDocument supporting = new ApprovalSupportingDocument();
                supporting.setDocumentName(docu.getDocumentName());
                supporting.setDocumentBase64String(docu.getDocumentBase64String());
                try {
                    saveSupportingDocument(supporting,
                            model.getApplicationId(),
                            model.getWorkflowStepNum(),
                            model.getWorkFlowStepName(),
                            model.getProcess());
                } catch (SQLException e) {
                    log.info("Supporting document exception {}", e.getMessage());
                }
            });
        }

        if (model.getRejectedNumbers() != null) {
            List<String> rejectedNumbers = model.getRejectedNumbers();
            // update the `IsNoRejected` flag to `TRUE` in `SelectedNumbers` table
            rejectedNumbers.forEach(rej -> {
                // Update ApplicationStatus column to Approved
                try {
                    int updateNoToRejected = genericTableUpdateRepository.
                            updateTableColumnWithTwoConditions("SelectedNumbers",
                                    "IsNoRejected",
                                    "TRUE",
                                    "ApplicationId",
                                    model.getApplicationId(),
                                    "SelectedNumberValue",
                                    rej);
                    log.info("Selected number is updated to rejected {}", updateNoToRejected);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            // Convert array of string to comma seperated string
            rejectedString = String.join(",", rejectedNumbers);
            log.info("Rejected number {} ", rejectedString);
        }

        String sql2 = "UPDATE ApplicationApprovalTracking set ApprovalAction = ?, AuthorizerUsername  = ?, " +
                "AuthorizerEmail =?, AssignedDate=?, Comment=?, RejectedNumbers=?, ReplacementUrl=? WHERE ApplicationId=? AND WorkFlowStepNum=?";
        return jdbcTemplate.update(sql2,
                model.getApprovalAction(),
                model.getAuthorizerUsername(),
                model.getAuthorizerEmail(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                model.getComment(),
                rejectedString,
                model.getReplacementUrl(),
                model.getApplicationId(),
                model.getWorkflowStepNum()
        );
    }

    /**
     * Method to save approval supporting document
     *
     * @param model
     * @param applicationId
     * @param workFlowStepNum
     * @param workFlowStepName
     * @param process
     * @return
     * @throws SQLException
     */
    public int saveSupportingDocument(ApprovalSupportingDocument model,
                                      String applicationId,
                                      String workFlowStepNum,
                                      String workFlowStepName,
                                      String process) throws SQLException {
        String sql = "INSERT INTO ApprovalSupportingDocument (ApplicationId, DocumentName," +
                " DocumentBase64String, CreatedDate, WorkFlowStepNum, WorkFlowStepName,  Process) " +
                "VALUES(?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                applicationId,
                model.getDocumentName(),
                model.getDocumentBase64String(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                workFlowStepNum,
                workFlowStepName,
                process
        );
    }

    /**
     * Method to get application approval process
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    public List<ApproveApplicationObject> getApplicationApprovalProcess(String applicationId) throws SQLException {
        String sql2 = "SELECT * from ApplicationApprovalTracking WHERE ApplicationId=?";
        List<ApproveApplicationObject> approvalProcess = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(ApproveApplicationObject.class), applicationId);
        log.info("ApprovalProcess {} ", approvalProcess);
        return approvalProcess;
    }


    /**
     * Method to get application approval supporting document
     *
     * @param applicationId
     * @param workFlowStepNum
     * @return
     * @throws SQLException
     */
    public List<ApprovalSupportingDocument> getApplicationSupportingDoc(String applicationId, String workFlowStepNum) throws SQLException {
        String sql = "SELECT * from ApprovalSupportingDocument WHERE ApplicationId=? AND WorkFlowStepNum=?";
        List<ApprovalSupportingDocument> supportingDocuments = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ApprovalSupportingDocument.class), applicationId, workFlowStepNum);
        return supportingDocuments;
    }


    /**
     * Method to check if application step is already approved
     *
     * @param model
     * @return
     * @throws SQLException
     */
    public int checkIfAlreadyApproved(ApproveApplicationModel model) throws SQLException {
        try {
            String sql = "SELECT count(*) FROM ApplicationApprovalTracking WHERE ApplicationId=? AND WorkFlowStepNum=? AND Process=? AND WorkFlowStepName=? AND ApprovalAction != ''";
            int res = jdbcTemplate.queryForObject(sql,
                    Integer.class,
                    model.getApplicationId(),
                    model.getWorkflowStepNum(),
                    model.getProcess(),
                    model.getWorkFlowStepName()
            );
            log.info("sql {}", sql);
            log.info("Check if already approved count {}", res);
            return res;
        } catch (Exception ex) {
            log.info("Exception occurred {}", ex.getMessage());
        }
        return -1;
    }


    /**
     * Method checks if all approval step is already approved and approval is completed
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    public boolean checkIfApprovalIsCompleted(String applicationId) throws SQLException {
        try {
            String sql1 = "SELECT count(*) FROM ApplicationApprovalTracking WHERE ApplicationId=?";
            String sql2 = "SELECT count(*) FROM ApplicationApprovalTracking WHERE ApplicationId=? AND ApprovalAction = 'APPROVED'";

            int noOfStepsInApplication = jdbcTemplate.queryForObject(sql1,
                    Integer.class,
                    applicationId
            );
            log.info("No of steps in application {}", noOfStepsInApplication);
            int noOfApprovedSteps = jdbcTemplate.queryForObject(sql2,
                    Integer.class,
                    applicationId
            );
            log.info("No of approved steps application {}", noOfApprovedSteps);

            return noOfApprovedSteps > 1 && Objects.equals(noOfStepsInApplication, noOfApprovedSteps);
        } catch (Exception ex) {
            log.info("Exception occurred {}", ex.getMessage());
            return false;
        }
    }

    public int deleteRejectedApprovalSteps(String applicationId) throws SQLException {
        String sql = "UPDATE ApplicationApprovalTracking SET AuthorizerUsername=NULL, AuthorizerEmail=NULL, ApprovalAction=NULL, RejectedNumbers=NULL, ReplacementUrl=NULL WHERE ApplicationId=? AND ApprovalAction='REJECTED'";
        return jdbcTemplate.update(sql, applicationId);
    }

    public String getReplacementUrl(String applicationId) {
        String sql = "SELECT ReplacementUrl FROM ApplicationApprovalTracking WHERE ApplicationId=? AND ApprovalAction='REJECTED'";

        String streetName = jdbcTemplate.queryForObject(
                sql, new Object[]{applicationId}, String.class);

        return streetName;
    }
}
