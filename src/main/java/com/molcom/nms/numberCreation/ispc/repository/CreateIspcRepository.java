package com.molcom.nms.numberCreation.ispc.repository;

import com.molcom.nms.adminmanage.repository.IAdminManagementRepo;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.numberCreation.ispc.dto.BulkUploadItem;
import com.molcom.nms.numberCreation.ispc.dto.BulkUploadRequestIspc;
import com.molcom.nms.numberCreation.ispc.dto.BulkUploadResponse;
import com.molcom.nms.numberCreation.ispc.dto.CreateIspcNoModel;
import com.molcom.nms.sendBulkEmail.dto.EservicesSendMailRequest;
import com.molcom.nms.sendBulkEmail.service.IBulkEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class CreateIspcRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IBulkEmailService bulkEmailService;

    @Autowired
    private IAdminManagementRepo adminManagementRepo;

    public int countIfNoExist(String id) throws SQLException {
        String sql = "SELECT count(*) FROM CreatedIspcNumber WHERE IspcNumber=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, id);
    }

    public int createSingleNumber(CreateIspcNoModel model) throws SQLException {
        String sql = "INSERT INTO CreatedIspcNumber (IspcNumber, CreatedBy,CreatedDate, Quantity, AvailableCount) " +
                "VALUES(?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                model.getIspcNumber(),
                model.getCreatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                "1",
                "1"
        );
    }

    public BulkUploadResponse createBulkNumber(BulkUploadRequestIspc bulkUploadRequest) throws SQLException {
        BulkUploadResponse bulkUploadResponse = new BulkUploadResponse();
        List<BulkUploadItem> bulkUploadItem = new ArrayList<>();
        bulkUploadRequest.getBulkList().forEach(bulkItem -> {
            CreateIspcNoModel model = new CreateIspcNoModel();
            BulkUploadItem item = new BulkUploadItem();
            model.setIspcNumber(bulkItem.getIspcNumber());
            model.setCreatedBy(bulkUploadRequest.getBatchId());
            model.setCreatedDate(bulkItem.getCreatedDate());
            try {
                int isExist = countIfNoExist(model.getIspcNumber());
                if (isExist >= 1) {
                    item.setItemId(bulkItem.getIspcNumber());
                    item.setItemResCode("000");
                    item.setItemResMessage("ISPC number already exist");
                } else {
                    int responseCode = createSingleNumber(model);
                    log.info("AutoFeeResponse code for insert of {} ", bulkItem.getIspcNumber());
                    // Checking if the insert was successful for the individual bulk upload
                    if (Objects.equals(responseCode, 1)) {
                        item.setItemId(bulkItem.getIspcNumber());
                        item.setItemResCode("000");
                        item.setItemResMessage("SUCCESS");
                    } else {
                        // Checking if the insert failed for the individual bulk upload
                        item.setItemId(bulkItem.getIspcNumber());
                        item.setItemResCode("999");
                        item.setItemResMessage("Error occurred, please try later");
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
    boolean sendMailIfBulkUploadFails(BulkUploadResponse bulkUploadResponse, BulkUploadRequestIspc bulkUploadRequest) {
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

//            // pick created user from the 1st element in array
//            String createdUser = bulkUploadRequest.getBulkList().get(0).getCreatedBy();
//            log.info("Created User:  {}", createdUser);
//            companyName = adminManagementRepo.getEmailOrganisation(createdUser);
//            log.info("Company name {}", createdUser);
//            adminEmails = adminManagementRepo.getEmailOfFellowAdmin(createdUser);
//            log.info("Admin Emails {}", adminEmails);
//            String company = (companyName != null ? companyName.toUpperCase() : "");
//            log.info("Company Name {}", companyName);

            totalCount = bulkUploadResponse.getAllList().size();
            successCount = success.size();
            totalFailure = duplicate.size() + internalServerError.size();
            duplicateCount = duplicate.size();
            internalErrorCount = internalServerError.size();

            adminEmails = adminManagementRepo.getAdminEmails();
            log.info("Admin Emails {}", adminEmails);

            EservicesSendMailRequest mailRequest = new EservicesSendMailRequest();
            mailRequest.setRecipients(adminEmails);
            mailRequest.setSubject("ISPC NUMBER CREATION BULK UPLOAD ERROR BREAKDOWN");

            mailRequest.setBody("<h3>Some item failed during ISPC Number Creation bulk upload, Please find error breakdown</h3>" +
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

    public CreateIspcNoModel getNumberById(String id) throws SQLException {
        String sql = "SELECT * FROM CreatedIspcNumber WHERE CreatedIspcNumberId = ?";
        try {
            CreateIspcNoModel createIspcNoModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(CreateIspcNoModel.class), id);
            log.info("ReportIspcNoModel {} ", createIspcNoModel);
            return createIspcNoModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    public int deleteNumberById(String id) throws SQLException {
        String sql = "DELETE FROM CreatedIspcNumber WHERE CreatedIspcNumberId = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<CreateIspcNoModel> getAllNumber() throws SQLException {
        String sql = "SELECT * from CreatedIspcNumber";
        List<CreateIspcNoModel> createIspcNoModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(CreateIspcNoModel.class));
        log.info("IspcNoModel {} ", createIspcNoModels);
        return createIspcNoModels;
    }


    public List<CreateIspcNoModel> getAvailableIspcNumber() throws SQLException {
        String sql = "SELECT * from CreatedIspcNumber WHERE AvailableCount != '0'";
        List<CreateIspcNoModel> createIspcNoModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(CreateIspcNoModel.class));
        log.info("AvailableIspcNoModel {} ", createIspcNoModels);
        return createIspcNoModels;
    }

    public List<CreateIspcNoModel> filterNumber(String startDate, String endDate, String rowNumber) throws SQLException {
//        String sql = "SELECT * FROM CreatedIspcNumber WHERE DATE(CreatedDate) BETWEEN '" + startDate + "' AND '" + endDate + "' ORDER BY CreatedDate ASC LIMIT " + rowNumber + "";
//
//        log.info(sql);
//        List<CreateIspcNoModel> createIspcNoModelsList = jdbcTemplate.query(sql,
//                BeanPropertyRowMapper.newInstance(CreateIspcNoModel.class));
//        log.info("IspcNoModel {} ", createIspcNoModelsList);
//        return createIspcNoModelsList;

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from CreatedIspcNumber WHERE ");

        if (startDate != null && endDate != null &&
                !startDate.isEmpty() &&
                !endDate.isEmpty()) {
            sqlBuilder.append("DATE(CreatedDate) BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "') AND ");
        }

        sqlBuilder.append("CreatedDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        List<CreateIspcNoModel> createIspcNoModelsList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(CreateIspcNoModel.class));
        log.info("IspcNoModel {} ", createIspcNoModelsList);
        return createIspcNoModelsList;


    }

    public List<CreateIspcNoModel> getUnallocatedNumbers() throws SQLException {
        String sql = "SELECT * from CreatedIspcNumber WHERE UPPER(AllocationStatus) != 'ALLOCATED'";
        try {
            List<CreateIspcNoModel> createIspcNoModels = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(CreateIspcNoModel.class));
            return createIspcNoModels;

        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }
}
