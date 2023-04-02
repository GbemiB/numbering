package com.molcom.nms.coveragearea.repository;

import com.molcom.nms.adminmanage.repository.IAdminManagementRepo;
import com.molcom.nms.coveragearea.dto.BulkUploadItem;
import com.molcom.nms.coveragearea.dto.BulkUploadRequest;
import com.molcom.nms.coveragearea.dto.BulkUploadResponse;
import com.molcom.nms.coveragearea.dto.CoverageAreaModel;
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
public class CoverageAreaRepo implements ICoverageAreaRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IBulkEmailService bulkEmailService;

    @Autowired
    private IAdminManagementRepo adminManagementRepo;

    @Override
    public int accessCodeCount(String coverageName) throws SQLException {
        String sql = "SELECT count(*) FROM CoverageArea WHERE CoverageName=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, coverageName);
    }


    /**
     * Data layer to persist new coverage area
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int save(CoverageAreaModel model) throws SQLException {
        String sql = "INSERT INTO CoverageArea (CoverageName, CoverageType, CoverageDescription, CreatedUser, CreatedDate) " +
                "VALUES(?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                model.getCoverageName(),
                model.getCoverageType().toUpperCase(),
                model.getCoverageDescription(),
                model.getCreatedUser(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }


    /**
     * Data layer to edit existing coverage area
     *
     * @param model
     * @param coverageId
     * @return
     * @throws SQLException
     */
    @Override
    public int edit(CoverageAreaModel model, int coverageId) throws SQLException {
        int editRes = 0;

        String sql = "SELECT count(*) FROM CoverageArea WHERE CoverageName=?";
        int checkCountForEdit = jdbcTemplate.queryForObject(sql, Integer.class, model.getCoverageName());

        if (checkCountForEdit < 1) {
            String sql2 = "UPDATE CoverageArea set CoverageName = ?, CoverageType  = ?, CoverageDescription  = ?, " +
                    "UpdatedBy = ?, UpdatedDate =? WHERE CoverageId = ?";

            editRes = jdbcTemplate.update(sql2,
                    model.getCoverageName(),
                    model.getCoverageType(),
                    model.getCoverageDescription(),
                    model.getUpdatedBy(),
                    CurrentTimeStamp.getCurrentTimeStamp(),
                    coverageId
            );
        }
        return editRes;

    }

    /**
     * Data layer to delete existing coverage area
     *
     * @param coverageId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteById(int coverageId) throws SQLException {
        String sql = "DELETE FROM CoverageArea WHERE CoverageId = ?";
        return jdbcTemplate.update(sql, coverageId);
    }

    /**
     * Data layer to find existing coverage area bu id
     *
     * @param coverageId
     * @return
     * @throws SQLException
     */
    @Override
    public CoverageAreaModel findById(int coverageId) throws SQLException {
        String sql = "SELECT * FROM CoverageArea WHERE CoverageId = ?";
        try {
            CoverageAreaModel coverageAreaModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(CoverageAreaModel.class), coverageId);
            log.info("CoverageAreaModel {} ", coverageAreaModel);
            return coverageAreaModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    /**
     * Data layer to get all existing coverage areas
     *
     * @return
     * @throws SQLException
     */
    @Override
    public List<CoverageAreaModel> getAll() throws SQLException {
        String sql = "SELECT * from CoverageArea ORDER BY CoverageName ASC";
        List<CoverageAreaModel> coverageAreaModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(CoverageAreaModel.class));
        return coverageAreaModels;
    }

    @Override
    public List<CoverageAreaModel> getByCoverageType(String coverageType) throws SQLException {
        String covArea = "";
        covArea = (coverageType != null ? coverageType.toUpperCase() : "");
        String sql = "SELECT * from CoverageArea WHERE CoverageType=? ORDER BY CoverageName ASC";
        List<CoverageAreaModel> coverageAreaModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(CoverageAreaModel.class), covArea);
        return coverageAreaModels;
    }

    /**
     * Data layer to insert coverage area in bulk
     *
     * @param bulkUploadRequest
     * @return
     * @throws SQLException
     */
    @Override
    public BulkUploadResponse bulkInsert(BulkUploadRequest bulkUploadRequest) throws SQLException {
        log.info("Bulk insert");
        BulkUploadResponse bulkUploadResponse = new BulkUploadResponse();
        List<BulkUploadItem> bulkUploadItem = new ArrayList<>();
        bulkUploadRequest.getBulkList().forEach(bulkItem -> {
            CoverageAreaModel model = new CoverageAreaModel();
            BulkUploadItem item = new BulkUploadItem();
            model.setCoverageId(bulkItem.getCoverageId());
            model.setCoverageName(bulkItem.getCoverageName());
            model.setCoverageType(bulkItem.getCoverageType());
            model.setCoverageDescription(bulkItem.getCoverageDescription());
            model.setCreatedUser(bulkUploadRequest.getBatchId());
            try {
                int isExist = accessCodeCount(model.getCoverageName());
                if (isExist >= 1) {
                    item.setItemId(bulkItem.getCoverageName());
                    item.setItemResCode(ResponseStatus.ALREADY_EXIST.getCode());
                    item.setItemResMessage("Coverage code already exist");
                } else {
                    int responseCode = save(model);
                    log.info("AutoFeeResponse code for insert of {} ", bulkItem.getCoverageName());
                    // Checking if the insert was successful for the individual bulk upload
                    if (Objects.equals(responseCode, 1)) {
                        item.setItemId(bulkItem.getCoverageName());
                        item.setItemResCode("000");
                        item.setItemResMessage("SUCCESS");
                    } else {
                        // Checking if the insert failed for the individual bulk upload
                        item.setItemId(bulkItem.getCoverageName());
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

    // Mail section
    @Async
    boolean sendMailIfBulkUploadFails(BulkUploadResponse bulkUploadResponse, BulkUploadRequest bulkUploadRequest) {
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
//            String createdUser = bulkUploadRequest.getBulkList().get(0).getCreatedUser();
//            log.info("Created User:  {}", createdUser);
//            String company = (companyName != null ? companyName.toUpperCase() : "");
//            log.info("Company Name {}", companyName);
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
            mailRequest.setSubject("COVERAGE AREA BULK UPLOAD ERROR BREAKDOWN");

            mailRequest.setBody("<h3>Some item failed during Coverage Area bulk upload, Please find error breakdown</h3>" +
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
     * Data layer to filterForRegularUser existing coverage area
     *
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<CoverageAreaModel> filter(String queryParam1, String queryValue1, String queryParam2, String queryValue2, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from CoverageArea WHERE ");

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

        sqlBuilder.append("CreatedDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();
        log.info("SQL {} ", sql);

        List<CoverageAreaModel> coverageAreaModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(CoverageAreaModel.class));
        log.info("CoverageAreaModel {} ", coverageAreaModels);
        return coverageAreaModels;
    }
}
