package com.molcom.nms.numberCreation.shortcode.repository;

import com.molcom.nms.adminmanage.repository.IAdminManagementRepo;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.numberCreation.shortcode.dto.BulkUploadExistingShortCode;
import com.molcom.nms.numberCreation.shortcode.dto.BulkUploadItem;
import com.molcom.nms.numberCreation.shortcode.dto.BulkUploadResponse;
import com.molcom.nms.numberCreation.shortcode.dto.CreateShortCodeNoModel;
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
public class CreateShortCodeRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IBulkEmailService bulkEmailService;

    @Autowired
    private IAdminManagementRepo adminManagementRepo;

    public int countIfNoExist(String minNumber, String maxNumber, String shortCodeService) throws SQLException {
        String sql = "SELECT count(*) FROM CreatedShortCodeNumber WHERE MinimumNumber =? AND MaximumNumber=?AND ShortCodeService=?";
        log.info(sql);
        return jdbcTemplate.queryForObject(
                sql, Integer.class, minNumber, maxNumber, shortCodeService);
    }

    public int validateLength(String minNumber, String maxNumber, String shortCodeCategory) throws SQLException {
        log.info("Number category {}", shortCodeCategory);
        if (shortCodeCategory != null && shortCodeCategory.trim().toUpperCase().contains("3")) {
            if (minNumber.length() == 3 && maxNumber.length() == 3) {
                return 1;
            } else {
                return -1;
            }

        } else if (shortCodeCategory != null && shortCodeCategory.trim().toUpperCase().contains("4")) {
            if (minNumber.length() == 4 && maxNumber.length() == 4) {
                return 1;
            } else {
                return -1;
            }
        } else if (shortCodeCategory != null && shortCodeCategory.trim().toUpperCase().contains("5")) {

            if (minNumber.length() == 5 && maxNumber.length() == 5) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public int createSingleNumber(CreateShortCodeNoModel model) throws SQLException {
        String sql = "INSERT INTO CreatedShortCodeNumber (NumberType, ShortCodeCategory,ShortCodeService,MinimumNumber,MaximumNumber," +
                "CreatedBy, CreatedDate, Quantity ) " +
                "VALUES(?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                "Short-Code",
                model.getShortCodeCategory(),
                model.getShortCodeService(),
                model.getMinimumNumber(),
                model.getMaximumNumber(),
                model.getCreatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                "1"
        );
    }

    public int saveNumberBlock(String numberType,
                               String numberSubType, String numberRange,
                               String numberBlock) throws SQLException {
        String sql = "INSERT INTO NumberBlock (NumberType,NumberSubType,NumberRange,NumberBlock," +
                "IsSelected ) " +
                "VALUES(?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                numberType,
                numberSubType,
                numberRange,
                numberBlock,
                "FALSE"
        );
    }

    public List<CreateShortCodeNoModel> getNumberByService(String shortCodeService) throws SQLException {
        String sql = "SELECT * from CreatedShortCodeNumber WHERE ShortCodeService = ?";
        try {
            List<CreateShortCodeNoModel> CreateSpecialNoModels = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(CreateShortCodeNoModel.class), shortCodeService);
            log.info("ReportShortCodeNoModel {} ", CreateSpecialNoModels);
            return CreateSpecialNoModels;

        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

//    public int deleteNumberById(String id) throws SQLException {
//        String sql = "DELETE FROM CreatedShortCodeNumber WHERE CreatedShortCodeNumber = ?";
//        return jdbcTemplate.update(sql, id);
//    }

    public int deleteNumberById(String id) throws SQLException {
        String min = "";
        String max = "";
        String number = "";
        String subType = "";

        String sql1 = "SELECT * from CreatedShortCodeNumber WHERE CreatedShortCodeNumberId=?";
        List<CreateShortCodeNoModel> createShortCodeNoModels = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(CreateShortCodeNoModel.class), id);
        log.info("CreateShortCodeNoModel {} ", createShortCodeNoModels);
        if (createShortCodeNoModels.size() > 0) {
            CreateShortCodeNoModel data = createShortCodeNoModels.get(0);
            min = data.getMinimumNumber();
            max = data.getMaximumNumber();
            number = min + "-" + max;
            subType = data.getShortCodeService();
            log.info("Number {}", number);

        }
        String sql2 = "DELETE FROM CreatedShortCodeNumber WHERE CreatedShortCodeNumberId = ?";
        int deleteNumberRange = jdbcTemplate.update(sql2, id);
        log.info("Number range delete {}", deleteNumberRange);

        String sql3 = "DELETE FROM NumberBlock WHERE NumberRange=? AND NumberSubType=?";
        int deleteNumberBlock = jdbcTemplate.update(sql3, number, subType);
        log.info("Number block delete {}", deleteNumberBlock);

        if (deleteNumberBlock >= 1 && deleteNumberRange >= 1) {
            return 1;
        } else {
            return -1;
        }
    }


    public List<CreateShortCodeNoModel> getAllNumber() throws SQLException {
        String sql = "SELECT * from CreatedShortCodeNumber";
        List<CreateShortCodeNoModel> CreateSpecialNoModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(CreateShortCodeNoModel.class));
        log.info("ReportShortCodeNoModel {} ", CreateSpecialNoModels);
        return CreateSpecialNoModels;
    }

    public List<CreateShortCodeNoModel> getAvailableShortCodeNumber(String shortCodeService) throws SQLException {
        String sql = "SELECT * from CreatedShortCodeNumber WHERE UPPER(ShortCodeService)=?";
        List<CreateShortCodeNoModel> CreateSpecialNoModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(CreateShortCodeNoModel.class), shortCodeService);
        log.info("ReportShortCodeNoModel {} ", CreateSpecialNoModels);
        return CreateSpecialNoModels;
    }


    public List<CreateShortCodeNoModel> filterNumber(String category, String service, String startDate, String endDate, String rowNumber) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from CreatedShortCodeNumber WHERE ");

        if (category != null && !category.isEmpty() &&
                !Objects.equals(category, "")
                && !Objects.equals(category.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ShortCodeCategory) = '").append("" + category.toUpperCase() + "").append("' AND ");
        }

        if (service != null && !service.isEmpty() &&
                !Objects.equals(service, "")
                && !Objects.equals(service.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ShortCodeService) = '").append("" + service.toUpperCase() + "").append("' AND ");
        }

        if (startDate != null && endDate != null &&
                !startDate.isEmpty() &&
                !endDate.isEmpty()) {
            sqlBuilder.append("DATE(CreatedDate) BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "') AND ");
        }

        sqlBuilder.append("CreatedDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        List<CreateShortCodeNoModel> CreateSpecialNoModelsList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(CreateShortCodeNoModel.class));
        log.info("ReportShortCodeNoModel {} ", CreateSpecialNoModelsList);
        return CreateSpecialNoModelsList;
    }

    public BulkUploadResponse uploadExistingShortCode(BulkUploadExistingShortCode bulkUploadRequest) throws SQLException {
        log.info("Bulk insert");
        BulkUploadResponse bulkUploadResponse = new BulkUploadResponse();
        List<BulkUploadItem> bulkUploadItem = new ArrayList<>();
        bulkUploadRequest.getBulkList().forEach(bulkItem -> {
            CreateShortCodeNoModel model = new CreateShortCodeNoModel();
            BulkUploadItem item = new BulkUploadItem();
            model.setNumberType("Short-Code");
            model.setShortCodeCategory(bulkItem.getServiceType());
            model.setShortCodeService(bulkItem.getService());
            model.setMinimumNumber(bulkItem.getShortCode());
            model.setMaximumNumber(bulkItem.getShortCode());
            model.setCreatedBy(bulkItem.getOperator());
            model.setCreatedDate(bulkItem.getDateAllocated());
            try {
                int countIfNoExist = countIfNoExist(model.getMinimumNumber(), model.getMaximumNumber(), model.getShortCodeService());
                if (countIfNoExist >= 1) {
                    item.setItemId(bulkItem.getShortCode());
                    item.setItemResCode("000");
                    item.setItemResMessage("Short code already exist");
                } else {
                    int responseCode = createSingleNumber(model);
                    log.info("AutoFeeResponse code for insert of {} ", bulkItem.getShortCode());
                    // Checking if the insert was successful for the individual bulk upload
                    if (Objects.equals(responseCode, 1)) {
                        item.setItemId(bulkItem.getShortCode());
                        item.setItemResCode("000");
                        item.setItemResMessage("SUCCESS");
                    } else {
                        // Checking if the insert failed for the individual bulk upload
                        item.setItemId(bulkItem.getShortCode());
                        item.setItemResCode("999");
                        item.setItemResMessage("FAILED TO INSERT RECORD TO");
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

        sendMailForExistingShortCode(bulkUploadResponse, bulkUploadRequest);

        return bulkUploadResponse;
    }

    // Mail section
    @Async
    boolean sendMailForExistingShortCode(BulkUploadResponse bulkUploadResponse, BulkUploadExistingShortCode bulkUploadRequest) {
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
//            String createdUser = bulkUploadRequest.getBulkList().get(0).getClient();
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
            mailRequest.setSubject("EXISTING SHORT CODE BULK UPLOAD ERROR BREAKDOWN");

            mailRequest.setBody("<h3>Some item failed during Existing Short Code bulk upload, Please find error breakdown</h3>" +
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


    public List<CreateShortCodeNoModel> getUnallocatedNumbers() throws SQLException {
        String sql = "SELECT * from CreatedShortCodeNumber WHERE UPPER(AllocationStatus) != 'ALLOCATED'";
        try {
            List<CreateShortCodeNoModel> createShortCodeNoModels = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(CreateShortCodeNoModel.class));
            return createShortCodeNoModels;

        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

}
