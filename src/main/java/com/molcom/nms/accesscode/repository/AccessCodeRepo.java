package com.molcom.nms.accesscode.repository;

import com.molcom.nms.accesscode.dto.AccessCodeBlkItem;
import com.molcom.nms.accesscode.dto.AccessCodeBlkReq;
import com.molcom.nms.accesscode.dto.AccessCodeBlkRes;
import com.molcom.nms.accesscode.dto.AccessCodeModel;
import com.molcom.nms.adminmanage.repository.IAdminManagementRepo;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
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
public class AccessCodeRepo implements IAccessCodeRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IBulkEmailService bulkEmailService;

    @Autowired
    private IAdminManagementRepo adminManagementRepo;

    @Override
    public int accessCodeCount(String accessCode) throws SQLException {
        String sql = "SELECT count(*) FROM AccessCode WHERE AccessCodeName=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, accessCode);
    }

    /**
     * Data layer to save access code
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int save(AccessCodeModel model) throws SQLException {
        String sql = "INSERT INTO AccessCode (AccessCodeName, AreaCode, NumberType, NumberSubType, CoverageArea, Status, CreatedUser, CreatedDate) VALUES(?,?,?,?,?,?,?,?)";
        log.info(sql);
        return jdbcTemplate.update(sql,
                model.getAccessCodeName(),
                model.getAreaCode(),
                model.getNumberType(),
                model.getNumberSubType(),
                model.getCoverageArea(),
                model.getStatus(),
                model.getCreatedUser(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * Data layer to edit access code
     *
     * @param model
     * @param accessCodeId
     * @return
     * @throws SQLException
     */
    @Override
    public int edit(AccessCodeModel model, int accessCodeId) throws SQLException {
        String sql = "UPDATE AccessCode set AccessCodeName=?, UpdatedBy =?, UpdatedDate = ? WHERE AccessCodeId = ? ";

        return jdbcTemplate.update(sql,
                model.getAccessCodeName(),
                model.getUpdatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                accessCodeId
        );
    }

    /**
     * Data layer to delete access code
     *
     * @param accessCodeId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteById(int accessCodeId) throws SQLException {
        String sql = "DELETE FROM AccessCode WHERE AccessCodeId = ?";
        return jdbcTemplate.update(sql, accessCodeId);
    }

    /**
     * Data layer to get access code by id
     *
     * @param accessCodeId
     * @return
     * @throws SQLException
     */
    @Override
    public AccessCodeModel findById(int accessCodeId) throws SQLException {
        String sql = "SELECT * FROM AccessCode WHERE AccessCodeId = ?";
        try {
            AccessCodeModel accessCodeModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(AccessCodeModel.class), accessCodeId);
            log.info("AccessCodeModel {} ", accessCodeModel);
            return accessCodeModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    /**
     * Data layer to get all access code
     *
     * @return
     * @throws SQLException
     */
    @Override
    public List<AccessCodeModel> getAll() throws SQLException {
        String sql = "SELECT * from AccessCode ORDER BY CreatedDate DESC";
        List<AccessCodeModel> accessCodeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AccessCodeModel.class));
        log.info("AccessCodeModel {} ", accessCodeModels);
        return accessCodeModels;
    }

    /**
     * Get access codes by coverage area
     *
     * @param coverageArea
     * @return
     * @throws SQLException
     */
    @Override
    public List<AccessCodeModel> getByCoverageName(String coverageArea) throws SQLException {
        String covArea = (coverageArea != null ? coverageArea.toUpperCase() : "");
        String sql = "SELECT * from AccessCode WHERE UPPER(CoverageArea)= '" + covArea + "' ORDER BY AccessCodeName ASC";
        List<AccessCodeModel> accessCodeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AccessCodeModel.class));
        return accessCodeModels;
    }

    /**
     * Get access codes by area code
     *
     * @param areaCode
     * @return
     * @throws SQLException
     */
    @Override
    public List<AccessCodeModel> getByAreaCode(String areaCode) throws SQLException {
        String area = (areaCode != null ? areaCode.toUpperCase() : "");
        String sql = "SELECT * from AccessCode WHERE UPPER(AreaCode)= '" + area + "' ORDER BY AccessCodeName ASC";
        List<AccessCodeModel> accessCodeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AccessCodeModel.class));
        return accessCodeModels;
    }

    /**
     * Datalayer to get access codes by number subtype
     *
     * @param numberSubType
     * @return
     * @throws SQLException
     */
    @Override
    public List<AccessCodeModel> getByNumberSubType(String numberSubType) throws SQLException {
        String subType = (numberSubType != null ? numberSubType.toUpperCase() : "");
        String sql = "SELECT * from AccessCode WHERE UPPER(NumberSubType)= '" + subType + "' ORDER BY AccessCodeName ASC";
        List<AccessCodeModel> accessCodeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AccessCodeModel.class));
        return accessCodeModels;
    }

    /**
     * Data layer to save bulk access code
     *
     * @param bulkUploadRequest
     * @return
     * @throws SQLException
     */
    @Override
    public AccessCodeBlkRes bulkInsert(AccessCodeBlkReq bulkUploadRequest) throws SQLException {
        log.info("Bulk insert");
        AccessCodeBlkRes bulkUploadResponse = new AccessCodeBlkRes();
        List<AccessCodeBlkItem> bulkUploadItem = new ArrayList<>();
        bulkUploadRequest.getBulkList().forEach(bulkItem -> {
            AccessCodeModel model = new AccessCodeModel();
            AccessCodeBlkItem item = new AccessCodeBlkItem();
            model.setAccessCodeName(bulkItem.getAccessCodeName());
            model.setAreaCode(bulkItem.getAreaCode());
            model.setCoverageArea(bulkItem.getCoverageArea());
            model.setNumberType(bulkItem.getNumberType());
            model.setNumberSubType(bulkItem.getNumberSubType());
            model.setCreatedUser(bulkUploadRequest.getBatchId());
            model.setStatus("ACTIVE");
            try {
                int count = accessCodeCount(model.getAccessCodeName());
                log.info("Checking bulk count {} for {}", count, model.getAccessCodeName());
                if (count >= 1) {
                    item.setItemId(bulkItem.getAccessCodeName());
                    item.setItemResCode(ResponseStatus.ALREADY_EXIST.getCode());
                    item.setItemResMessage("Access code already exist");
                } else {
                    int responseCode = save(model);
                    log.info("AutoFeeResponse code for insert of {} ", bulkItem.getAccessCodeName());
                    // Checking if the insert was successful for the individual bulk upload
                    if (Objects.equals(responseCode, 1)) {
                        item.setItemId(bulkItem.getAccessCodeName());
                        item.setItemResCode("000");
                        item.setItemResMessage("SUCCESS");
                    } else {
                        // Checking if the insert failed for the individual bulk upload
                        item.setItemId(bulkItem.getAccessCodeName());
                        item.setItemResCode("999");
                        item.setItemResMessage("Error occurred");
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

    /**
     * Mail service implementation
     *
     * @param bulkUploadResponse
     * @param bulkUploadRequest
     * @return
     */

    // Mail section
    @Async
    boolean sendMailIfBulkUploadFails(AccessCodeBlkRes bulkUploadResponse, AccessCodeBlkReq bulkUploadRequest) {
        // make it async for optimal performance and line separator
        List<AccessCodeBlkItem> success = bulkUploadResponse.getAllList().stream().filter(item -> item.getItemResCode().contains("000"))
                .collect(Collectors.toList());
        List<AccessCodeBlkItem> duplicate = bulkUploadResponse.getAllList().stream().filter(item -> item.getItemResCode().contains("666"))
                .collect(Collectors.toList());
        List<AccessCodeBlkItem> internalServerError = bulkUploadResponse.getAllList().stream().filter(item -> item.getItemResCode().contains("999"))
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
//            String createdUser = bulkUploadRequest.getBulkList().get(0).getCreatedUser();
//            log.info("Created User:  {}", createdUser);
//            companyName = adminManagementRepo.getEmailOrganisation(createdUser);
//            log.info("Company name {}", createdUser);
//            String company = (companyName != null ? companyName.toUpperCase() : "");
//            log.info("Company Name {}", companyName);
//            adminEmails = adminManagementRepo.getEmailOfFellowAdmin(createdUser);
//            log.info("Admin Emails {}", adminEmails);

            adminEmails = adminManagementRepo.getAdminEmails();
            log.info("Admin Emails {}", adminEmails);

            totalCount = bulkUploadResponse.getAllList().size();
            successCount = success.size();
            totalFailure = duplicate.size() + internalServerError.size();
            duplicateCount = duplicate.size();
            internalErrorCount = internalServerError.size();

            EservicesSendMailRequest mailRequest = new EservicesSendMailRequest();
            mailRequest.setRecipients(adminEmails);
            mailRequest.setSubject("ACCESS CODE BULK UPLOAD ERROR BREAKDOWN");

            mailRequest.setBody("<h3>Some item failed during Access Code bulk upload, Please find error breakdown</h3>" +
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
     * Data layer to filterForRegularUser access codes
     *
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param queryParam3
     * @param queryValue3
     * @param queryParam4
     * @param queryValue4
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<AccessCodeModel> filter(String queryParam1, String queryValue1, String queryParam2, String queryValue2, String queryParam3, String queryValue3, String queryParam4, String queryValue4, String rowNumber) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from AccessCode WHERE ");

        if (queryValue1 != null && !queryValue1.isEmpty() &&
                !Objects.equals(queryValue1, "")
                && !Objects.equals(queryValue1.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue1.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam1 + " = '").append("" + queryValue1 + "").append("' AND ");
        }


        if (queryValue2 != null && !queryValue2.isEmpty() &&
                !Objects.equals(queryValue2, "")
                && !Objects.equals(queryValue2.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue2.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam2 + " = '").append("" + queryValue2 + "").append("' AND ");
        }

        if (queryValue3 != null && !queryValue3.isEmpty() &&
                !Objects.equals(queryValue3, "")
                && !Objects.equals(queryValue3.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue3.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam3 + " = '").append("" + queryValue3 + "").append("' AND ");
        }

        if (queryValue4 != null && !queryValue4.isEmpty() &&
                !Objects.equals(queryValue4, "")
                && !Objects.equals(queryValue4.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue4.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam4 + " = '").append("" + queryValue4 + "").append("' AND ");
        }
        sqlBuilder.append("CreatedDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        log.info("SQL query {} ", sql);
        List<AccessCodeModel> accessCodeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AccessCodeModel.class));
        log.info("AccessCodeModel {} ", accessCodeModels);


        return accessCodeModels;
    }
}