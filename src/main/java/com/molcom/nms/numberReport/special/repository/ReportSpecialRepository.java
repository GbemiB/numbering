package com.molcom.nms.numberReport.special.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.invoice.dto.InvoiceModel;
import com.molcom.nms.invoice.repository.InvoiceRepository;
import com.molcom.nms.numberReport.special.dto.BulkUploadItem;
import com.molcom.nms.numberReport.special.dto.BulkUploadReportSpecial;
import com.molcom.nms.numberReport.special.dto.BulkUploadResponse;
import com.molcom.nms.numberReport.special.dto.ReportSpecialNoModel;
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
public class ReportSpecialRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private InvoiceRepository invoiceRepository;


    /**
     * @param minNumber
     * @param maxNumber
     * @param numberSubType
     * @return
     * @throws SQLException
     */
    public int countIfNoExist(String minNumber, String maxNumber, String numberSubType) throws SQLException {
        String sql = "SELECT count(*) FROM ReportSpecialNumber WHERE MinimumNumber=? AND MaximumNumber=? AND NumberSubType=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, minNumber, maxNumber, numberSubType);
    }

    /**
     * @return
     * @throws SQLException
     */
    public int updateAfterAllocation(String applicationId) throws SQLException {
        Timestamp now = CurrentTimeStamp.getCurrentTimeStamp();
        Timestamp next = CurrentTimeStamp.getNextYearTimeStamp(now, 365);
        // means the number has been reported already
        String sql2 = "UPDATE ReportSpecialNumber SET DateAllocated=?, AllocationValidityFrom=?," +
                "AllocationValidityTo=?, AllocationStatus=? WHERE ApplicationId=? ";
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
    public int internalReport(ReportSpecialNoModel model) throws SQLException {
        int count = countIfNoExist(model.getMinimumNumber(), model.getMaximumNumber(), model.getNumberSubType());

        if (count >= 1) {
            // means the number has been reported already
            String sql2 = "UPDATE ReportSpecialNumber SET DateAllocated =?, AllocationValidityFrom =?," +
                    "AllocationValidityTo =?, CompanyAllocatedTo =?, AllocationStatus=?, Purpose=?, BearerMedium=?  WHERE MinimumNumber=? AND MaximumNumber=?" +
                    "AND NumberSubType=? AND ApplicationId=? ";
            return jdbcTemplate.update(sql2,
                    model.getDateAllocated(),
                    model.getAllocationValidityFrom(),
                    model.getAllocationValidityTo(),
                    model.getCompanyAllocatedTo(),
                    model.getAllocationStatus(),
                    model.getPurpose(),
                    model.getBearerMedium(),
                    model.getMinimumNumber(),
                    model.getMaximumNumber(),
                    model.getNumberSubType(),
                    model.getApplicationId()
            );
        } else {

            String sql = "INSERT INTO ReportSpecialNumber (NumberType, NumberSubType, Allotee, AccessCode,MinimumNumber,MaximumNumber," +
                    "CreatedBy, CreatedDate, Quantity,AvailableCount,DateAllocated, AllocationValidityFrom, AllocationValidityTo, CompanyAllocatedTo,AllocationStatus,ApplicationId,Purpose,BearerMedium ) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            return jdbcTemplate.update(sql,
                    "Special",
                    model.getNumberSubType(),
                    model.getAllotee(),
                    model.getAccessCode(),
                    model.getMinimumNumber(),
                    model.getMaximumNumber(),
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
    public int createSingleNumber(ReportSpecialNoModel model) throws SQLException {

        String sql = "INSERT INTO ReportSpecialNumber (NumberType, NumberSubType, Allotee, AccessCode,MinimumNumber,MaximumNumber," +
                "CreatedBy, CreatedDate, Quantity,AvailableCount,AllocationStatus ) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                "Special",
                model.getNumberSubType(),
                model.getAllotee(),
                model.getAccessCode(),
                model.getMinimumNumber(),
                model.getMaximumNumber(),
                model.getCreatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                "10000000",
                "0",
                "ALLOCATED"
        );
    }

    /**
     * @param bulkUploadRequest
     * @return
     * @throws SQLException
     */
    public BulkUploadResponse createBulkNumber(BulkUploadReportSpecial bulkUploadRequest) throws SQLException {
        log.info("Bulk insert");
        BulkUploadResponse bulkUploadResponse = new BulkUploadResponse();
        List<BulkUploadItem> bulkUploadItem = new ArrayList<>();
        bulkUploadRequest.getBulkList().forEach(bulkItem -> {
            ReportSpecialNoModel model = new ReportSpecialNoModel();
            BulkUploadItem item = new BulkUploadItem();
            model.setNumberSubType("Special");
            model.setNumberSubType("Toll-free");
            model.setAllotee(bulkItem.getAllotee());
            model.setAccessCode(bulkItem.getAccessCode());
            model.setMinimumNumber(bulkItem.getSubBlockStart());
            model.setMaximumNumber(bulkItem.getSubBlockEnd());
            model.setCreatedBy(bulkUploadRequest.getBatchId());
            try {
                int count = countIfNoExist(model.getMinimumNumber(), model.getMaximumNumber(), model.getNumberSubType());
                if (count >= 1) {
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
     * @param numberSubType
     * @param accessCode
     * @param startDate
     * @param endDate
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    public List<ReportSpecialNoModel> filterNumber(String organisation, String numberSubType, String accessCode, String startDate, String endDate, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from ReportSpecialNumber WHERE ");

        if (organisation != null && !organisation.isEmpty() &&
                !Objects.equals(organisation, "")
                && !Objects.equals(organisation.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(CompanyAllocatedTo) = '").append("" + organisation.toUpperCase() + "").append("' AND ");
        }

        if (numberSubType != null && !numberSubType.isEmpty() &&
                !Objects.equals(numberSubType, "")
                && !Objects.equals(numberSubType.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(NumberSubType) = '").append("" + numberSubType.toUpperCase() + "").append("' AND ");
        }

        if (accessCode != null && !accessCode.isEmpty() &&
                !Objects.equals(accessCode, "")
                && !Objects.equals(accessCode.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(AccessCode) = '").append("" + accessCode.toUpperCase() + "").append("' AND ");
        }

        if (startDate != null && endDate != null &&
                !startDate.isEmpty() &&
                !endDate.isEmpty()) {
            sqlBuilder.append("DATE(CreatedDate) BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "') AND ");
        }

        sqlBuilder.append("CreatedDate IS NOT NULL AND AllocationStatus IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();
        log.info("sql {}", sql);

        List<ReportSpecialNoModel> CreateSpecialNoModelsList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ReportSpecialNoModel.class));
        log.info("ReportShortCodeNoModel {} ", CreateSpecialNoModelsList);
        return CreateSpecialNoModelsList;
    }


    /**
     * @param organisation
     * @return
     * @throws SQLException
     */
    public List<ReportSpecialNoModel> getByOrganisation(String organisation) throws SQLException {
        String org = (organisation != null ? organisation.trim().toUpperCase() : "");
        String sql = "SELECT * FROM ReportSpecialNumber WHERE UPPER(CompanyAllocatedTo)=? ORDER BY CreatedDate DESC";
        List<ReportSpecialNoModel> models = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ReportSpecialNoModel.class), org);
        return models;
    }

    /**
     * @return
     * @throws SQLException
     */
    public List<ReportSpecialNoModel> getAll() throws SQLException {
        String sql = "SELECT * FROM ReportSpecialNumber ORDER BY CreatedDate DESC";
        List<ReportSpecialNoModel> modelList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ReportSpecialNoModel.class));
        return modelList;
    }

    /**
     * @throws SQLException
     */
    public void updateAllocationStatus() throws SQLException {
        // Get list of ongoing allocation reports
        String sql1 = "SELECT * FROM ReportSpecialNumber WHERE UPPER(AllocationStatus)='ONGOING ALLOCATION'";
        List<ReportSpecialNoModel> modelList = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(ReportSpecialNoModel.class));
        log.info("Ongoing special allocation status update {} ", modelList);
        modelList.forEach(mod -> {
            try {
                List<InvoiceModel> list = invoiceRepository.getAllocationInvoice(mod.getApplicationId());
                // If status is PAID, update allocation status to allocated
                if (list != null && list.size() >= 1 && Objects.equals(list.get(0).getPaymentStatus(), "PAID")) {
                    String sql2 = "UPDATE ReportSpecialNumber SET AllocationStatus='ALLOCATED' WHERE ApplicationId=?";
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
