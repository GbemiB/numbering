package com.molcom.nms.areacode.repository;

import com.molcom.nms.adminmanage.repository.IAdminManagementRepo;
import com.molcom.nms.areacode.dto.AreaCodeBlkItem;
import com.molcom.nms.areacode.dto.AreaCodeBlkReq;
import com.molcom.nms.areacode.dto.AreaCodeBlkRes;
import com.molcom.nms.areacode.dto.AreaCodeModel;
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

@Repository
@Slf4j
public class AreaCodeRepo implements IAreaCodeRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IBulkEmailService bulkEmailService;

    @Autowired
    private IAdminManagementRepo adminManagementRepo;


    /**
     * Data layer to check if area code exist
     *
     * @param areaCode
     * @return
     * @throws SQLException
     */
    @Override
    public int areaCodeCount(String areaCode) throws SQLException {
        String sql = "SELECT count(*) FROM AreaCode WHERE AreaCode=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, areaCode);
    }

    /**
     * Data layer to save area code
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int save(AreaCodeModel model) throws SQLException {

        String sql = "INSERT INTO AreaCode (AreaCode, CoverageArea, CreatedUser, CreatedDate) VALUES(?,?,?,?)";
        return jdbcTemplate.update(sql,
                model.getAreaCode(),
                model.getCoverageArea(),
                model.getCreatedUser(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * Data layer to edit area code
     *
     * @param model
     * @param areaId
     * @return
     * @throws SQLException
     */
    @Override
    public int edit(AreaCodeModel model, int areaId) throws SQLException {
        String sql = "UPDATE AreaCode set AreaCode = ?, CoverageArea  = ?, UpdatedBy =?,  UpdatedDate =? " +
                "WHERE AreaId = ?";

        return jdbcTemplate.update(sql,
                model.getAreaCode(),
                model.getCoverageArea(),
                model.getUpdateBy(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                areaId
        );
    }


    /**
     * Data layer to delete area code
     *
     * @param areaId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteById(int areaId) throws SQLException {
        String sql = "DELETE FROM AreaCode WHERE AreaId = ?";
        return jdbcTemplate.update(sql, areaId);
    }

    /**
     * Data layer to get area code by id
     *
     * @param areaId
     * @return
     * @throws SQLException
     */
    @Override
    public AreaCodeModel findById(int areaId) throws SQLException {
        String sql = "SELECT * FROM AreaCode WHERE AreaId = ?";
        try {
            AreaCodeModel areaCodeModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(AreaCodeModel.class), areaId);
            return areaCodeModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    /**
     * Data layer to get all area codes
     *
     * @return
     * @throws SQLException
     */
    @Override
    public List<AreaCodeModel> getAll() throws SQLException {
        String sql = "SELECT * from AreaCode ORDER BY CreatedDate DESC";
        List<AreaCodeModel> areaCodeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AreaCodeModel.class));
        return areaCodeModels;
    }

    /**
     * Get area codes by coverage area
     *
     * @param coverageArea
     * @return
     * @throws SQLException
     */
    @Override
    public List<AreaCodeModel> getAreaCodeByCoverageArea(String coverageArea) throws SQLException {
        String cov = (coverageArea != null ? coverageArea.toUpperCase() : "");

        String sql = "SELECT * from AreaCode WHERE UPPER(CoverageArea)=? ORDER BY AreaCode ASC";
        List<AreaCodeModel> areaCodeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AreaCodeModel.class), cov);
        return areaCodeModels;
    }

    /**
     * Data layer to insert area codes in bulk
     *
     * @param bulkUploadRequest
     * @return
     * @throws SQLException
     */
    @Override
    public AreaCodeBlkRes bulkInsert(AreaCodeBlkReq bulkUploadRequest) throws SQLException {
        log.info("Bulk insert");
        AreaCodeBlkRes bulkUploadResponse = new AreaCodeBlkRes();
        List<AreaCodeBlkItem> bulkUploadItem = new ArrayList<>();
        bulkUploadRequest.getBulkList().forEach(bulkItem -> {
            AreaCodeModel model = new AreaCodeModel();
            AreaCodeBlkItem item = new AreaCodeBlkItem();
            model.setAreaCode(bulkItem.getAreaCode());
            model.setCoverageArea(bulkItem.getCoverageArea());
            model.setCreatedUser(bulkUploadRequest.getBatchId());
            try {
                int count = areaCodeCount(model.getAreaCode());
                log.info("Checking bulk count {} ", count);
                if (count >= 1) {
                    item.setItemId(bulkItem.getAreaCode());
                    item.setItemResCode(ResponseStatus.ALREADY_EXIST.getCode());
                    item.setItemResMessage("Area code already exist");
                } else {
                    int responseCode = save(model);
                    log.info("AutoFeeResponse code for insert of {} ", bulkItem.getBulkUploadId());
                    // Checking if the insert was successful for the individual bulk upload
                    if (Objects.equals(responseCode, 1)) {
                        item.setItemId(bulkItem.getAreaCode());
                        item.setItemResCode("000");
                        item.setItemResMessage("Area code inserted successfully");
                    } else {
                        // Checking if the insert failed for the individual bulk upload
                        item.setItemId(bulkItem.getAreaCode());
                        item.setItemResCode("999");
                        item.setItemResMessage("Error occurred, please try again later");
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
    boolean sendMailIfBulkUploadFails(AreaCodeBlkRes bulkUploadResponse, AreaCodeBlkReq bulkUploadRequest) {
        // make it async for optimal performance and line separator
        List<AreaCodeBlkItem> success = bulkUploadResponse.getAllList().stream().filter(item -> item.getItemResCode().contains("000"))
                .collect(Collectors.toList());
        List<AreaCodeBlkItem> duplicate = bulkUploadResponse.getAllList().stream().filter(item -> item.getItemResCode().contains("666"))
                .collect(Collectors.toList());
        List<AreaCodeBlkItem> internalServerError = bulkUploadResponse.getAllList().stream().filter(item -> item.getItemResCode().contains("999"))
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

//            companyName = adminManagementRepo.getEmailOrganisation(createdUser);
//            log.info("Company name {}", createdUser);
//            String company = (companyName != null ? companyName.toUpperCase() : "");
//            log.info("Company Name {}", companyName);
            // pick created user from the 1st element in array
//            String createdUser = bulkUploadRequest.getBulkList().get(0).getCreatedUser();
//            log.info("Created User:  {}", createdUser);
//            adminEmails = adminManagementRepo.getEmailOfFellowAdmin(createdUser);
//            log.info("Admin Emails {}", adminEmails);

            adminEmails = adminManagementRepo.getAdminEmails();
            log.info("Admin Emails {}", adminEmails);

            EservicesSendMailRequest mailRequest = new EservicesSendMailRequest();
            mailRequest.setRecipients(adminEmails);
            mailRequest.setSubject("AREA CODE BULK UPLOAD ERROR BREAKDOWN");

            mailRequest.setBody("<h3>Some item failed during Area Code bulk upload, Please find error breakdown</h3>" +
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
     * Data layer to filterForRegularUser area codes
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
    public List<AreaCodeModel> filter(String queryParam1, String queryValue1, String queryParam2,
                                      String queryValue2, String rowNumber) throws SQLException {
//        String sql = "SELECT * from AreaCode WHERE  " + queryParam1 + "  LIKE '%" + queryValue1 + "%' " + "" +
//                "OR " + queryParam2 + "  LIKE '%" + queryValue2 + "%'" + "ORDER BY AreaCode ASC LIMIT " + rowNumber + "";

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from AreaCode WHERE ");

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


        log.info("SQL script {} ", sql);
        List<AreaCodeModel> areaCodeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AreaCodeModel.class));
        return areaCodeModels;
    }
}
