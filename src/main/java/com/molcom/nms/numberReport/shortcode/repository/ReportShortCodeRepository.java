package com.molcom.nms.numberReport.shortcode.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.invoice.dto.InvoiceModel;
import com.molcom.nms.invoice.repository.InvoiceRepository;
import com.molcom.nms.numberReport.shortcode.dto.BulkUploadItem;
import com.molcom.nms.numberReport.shortcode.dto.BulkUploadReportShortCode;
import com.molcom.nms.numberReport.shortcode.dto.BulkUploadResponse;
import com.molcom.nms.numberReport.shortcode.dto.ReportShortCodeNoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class ReportShortCodeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private InvoiceRepository invoiceRepository;

    /**
     * @param shortCodeNumber
     * @return
     * @throws SQLException
     */
    public int countIfNoExist(String shortCodeNumber) throws SQLException {
        String sql = "SELECT count(*) FROM ReportShortCodeNumber WHERE ShortCodeNumber=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, shortCodeNumber);
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    public int updateAfterAllocation(String applicationId) throws SQLException {
        Timestamp now = CurrentTimeStamp.getCurrentTimeStamp();
        Timestamp next = CurrentTimeStamp.getNextYearTimeStamp(now, 365);
        // Update number to allocated
        String sql2 = "UPDATE ReportShortCodeNumber SET DateAllocated=?, AllocationValidityFrom=?," +
                "AllocationValidityTo=? AllocationStatus=? WHERE ApplicationId=?";
        return jdbcTemplate.update(sql2,
                now,
                now,
                next,
                "ALLOCATED",
                applicationId
        );
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    public int internalReport(ReportShortCodeNoModel model) throws SQLException {
        int count = countIfNoExist(model.getShortCodeNumber());

        if (count >= 1) {
            // means the number has been reported already
            String sql2 = "UPDATE ReportShortCodeNumber SET DateAllocated =?, AllocationValidityFrom =?," +
                    "AllocationValidityTo =?, CompanyAllocatedTo =?, AllocationStatus=?, Purpose=?, BearerMedium=? WHERE ShortCodeNumber=? AND ApplicationId=?";
            return jdbcTemplate.update(sql2,
                    model.getDateAllocated(),
                    model.getAllocationValidityFrom(),
                    model.getAllocationValidityTo(),
                    model.getCompanyAllocatedTo(),
                    model.getAllocationStatus(),
                    model.getPurpose(),
                    model.getBearerMedium(),
                    model.getShortCodeNumber(),
                    model.getApplicationId()
            );
        } else {

            String sql = "INSERT INTO ReportShortCodeNumber (NumberType, ShortCodeCategory,ShortCodeService,ShortCodeNumber," +
                    "CreatedBy, CreatedDate, Quantity,AvailableCount, DateAllocated, AllocationValidityFrom, AllocationValidityTo, CompanyAllocatedTo, AllocationStatus, ApplicationId, Purpose,BearerMedium ) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            return jdbcTemplate.update(sql,
                    "Short-Code",
                    model.getShortCodeCategory(),
                    model.getShortCodeService(),
                    model.getShortCodeNumber(),
                    model.getCreatedBy(),
                    CurrentTimeStamp.getCurrentTimeStamp(),
                    model.getQuantity(),
                    "0",
                    model.getDateAllocated(),
                    model.getAllocationValidityFrom(),
                    model.getAllocationValidityTo(),
                    model.getCompanyAllocatedTo(),
                    model.getAllocationStatus(),
                    model.getApplicationId(),
                    model.getPurpose(),
                    model.getBearerMedium()
            );
        }
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    public int createSingleNumber(ReportShortCodeNoModel model) throws SQLException {
        log.info("Short code single reporting {} ", model);
        String sql = "INSERT INTO ReportShortCodeNumber (NumberType, ShortCodeCategory,ShortCodeService,ShortCodeNumber," +
                "CreatedBy, CreatedDate, Quantity,AvailableCount,AllocationStatus ) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";
        log.info("Short code single reporting {} ", sql);
        int res = jdbcTemplate.update(sql,
                "Short-Code",
                model.getShortCodeCategory(),
                model.getShortCodeService(),
                model.getShortCodeNumber(),
                model.getCreatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                "1",
                "0",
                "ALLOCATED"
        );
        log.info("Short code report response {} ", res);
        return res;
    }

    /**
     * @param bulkUploadRequest
     * @return
     * @throws SQLException
     */
    public BulkUploadResponse createBulkNumber(BulkUploadReportShortCode bulkUploadRequest) throws SQLException {
        log.info("Bulk insert");
        BulkUploadResponse bulkUploadResponse = new BulkUploadResponse();
        List<BulkUploadItem> bulkUploadItem = new ArrayList<>();
        bulkUploadRequest.getBulkList().forEach(bulkItem -> {
            ReportShortCodeNoModel model = new ReportShortCodeNoModel();
            BulkUploadItem item = new BulkUploadItem();
            model.setNumberType("Short-Code");
            model.setShortCodeCategory("");
            model.setShortCodeService(bulkItem.getService());
            model.setShortCodeNumber(bulkItem.getShortCodeNumber());
            model.setCreatedBy(bulkUploadRequest.getBatchId());
            model.setCreatedDate("");
            log.info("Bulk insert payload {} ", model);
            try {
                int count = countIfNoExist(model.getShortCodeNumber());
                log.info("Bulk insert count {} ", count);
                if (count >= 1) {
                    // Checking if the insert failed for the individual bulk upload
                    item.setItemId(bulkItem.getBulkUploadId());
                    item.setItemResCode(ResponseStatus.ALREADY_EXIST.getCode());
                    item.setItemResMessage(ResponseStatus.ALREADY_EXIST.getMessage());
                } else {
                    int responseCode = createSingleNumber(model);
                    log.info("AutoFeeResponse code for insert of {} ", bulkItem.getBulkUploadId());
                    // Checking if the insert was successful for the individual bulk upload
                    if (Objects.equals(responseCode, 1)) {
                        item.setItemId(bulkItem.getBulkUploadId());
                        item.setItemResCode("000");
                        item.setItemResMessage("SUCCESS");
                    } else {
                        // Checking if the insert failed for the individual bulk upload
                        item.setItemId(bulkItem.getBulkUploadId());
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
        return bulkUploadResponse;
    }

    /**
     * @param organisation
     * @param category
     * @param service
     * @param startDate
     * @param endDate
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    public List<ReportShortCodeNoModel> filterNumber(String organisation, String category, String service, String startDate, String endDate, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from ReportShortCodeNumber WHERE ");

        if (organisation != null && !organisation.isEmpty() &&
                !Objects.equals(organisation, "")
                && !Objects.equals(organisation.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(CompanyAllocatedTo) = '").append("" + organisation.toUpperCase() + "").append("' AND ");
        }

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

        sqlBuilder.append("CreatedDate IS NOT NULL AND AllocationStatus IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        List<ReportShortCodeNoModel> CreateSpecialNoModelsList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ReportShortCodeNoModel.class));
        log.info("ReportShortCodeNoModel {} ", CreateSpecialNoModelsList);
        return CreateSpecialNoModelsList;
    }

    /**
     * @param organisation
     * @return
     * @throws SQLException
     */
    public List<ReportShortCodeNoModel> getByOrganisation(String organisation) throws SQLException {
        String org = (organisation != null ? organisation.trim().toUpperCase() : "");
        String sql = "SELECT * FROM ReportShortCodeNumber WHERE UPPER(CompanyAllocatedTo)=? ORDER BY CreatedDate DESC";
        List<ReportShortCodeNoModel> models = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ReportShortCodeNoModel.class), org);
        return models;
    }

    /**
     * @return
     * @throws SQLException
     */
    public List<ReportShortCodeNoModel> getAll() throws SQLException {
        String sql = "SELECT * FROM ReportShortCodeNumber ORDER BY CreatedDate DESC";
        List<ReportShortCodeNoModel> models = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ReportShortCodeNoModel.class));
        return models;
    }

    /**
     * @throws SQLException
     */
    public void updateAllocationStatus() throws SQLException {
        // Get list of ongoing allocation reports
        String sql1 = "SELECT * FROM ReportShortCodeNumber WHERE UPPER(AllocationStatus)='ONGOING ALLOCATION'";
        List<ReportShortCodeNoModel> modelList = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(ReportShortCodeNoModel.class));
        log.info("Ongoing short code allocation status update {} ", modelList);
        modelList.forEach(mod -> {
            try {
                List<InvoiceModel> list = invoiceRepository.getAllocationInvoice(mod.getApplicationId());
                // If status is PAID, update allocation status to allocated
                if (list != null && list.size() >= 1 && Objects.equals(list.get(0).getPaymentStatus(), "PAID")) {
                    String sql2 = "UPDATE ReportShortCodeNumber SET AllocationStatus='ALLOCATED' WHERE ApplicationId=?";
                    int statusUpdated = jdbcTemplate.update(sql2,
                            mod.getApplicationId());
                    log.info("Update of allocation status {} ", statusUpdated);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

}
