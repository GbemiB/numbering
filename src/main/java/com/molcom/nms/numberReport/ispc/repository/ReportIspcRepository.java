package com.molcom.nms.numberReport.ispc.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.invoice.repository.InvoiceRepository;
import com.molcom.nms.numberReport.ispc.dto.BulkUploadItem;
import com.molcom.nms.numberReport.ispc.dto.BulkUploadReportIspc;
import com.molcom.nms.numberReport.ispc.dto.BulkUploadResponse;
import com.molcom.nms.numberReport.ispc.dto.ReportIspcNoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class ReportIspcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private InvoiceRepository invoiceRepository;

    /**
     * @param minNumber
     * @param maxNumber
     * @return
     * @throws SQLException
     */
    public int countIfNoExist(String minNumber, String maxNumber) throws SQLException {
        String sql = "SELECT count(*) FROM ReportSpecialNumber WHERE MinimumNumber=? AND MaximumNumber=? ";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, minNumber, maxNumber);
    }

    /**
     * @param id
     * @return
     * @throws SQLException
     */
    public int countIfNoExist(String id) throws SQLException {
        String sql = "SELECT count(*) FROM ReportIspcNumber WHERE IspcNumber=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, id);
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    public int internalReport(ReportIspcNoModel model) throws SQLException {
        int count = countIfNoExist(model.getIspcNumber());
        log.info("Does Ispc report exist for application id {} ", count);
        if (count >= 1) {
            // means the number has been reported already
            String sql2 = "UPDATE ReportIspcNumber SET DateAllocated =?, AllocationValidityFrom =?," +
                    "AllocationValidityTo =?, CompanyAllocatedTo =?, AllocationStatus=? WHERE IspcNumber=? AND ApplicationId=?";
            return jdbcTemplate.update(sql2,
                    model.getDateAllocated(),
                    model.getAllocationValidityFrom(),
                    model.getAllocationValidityTo(),
                    model.getCompanyAllocatedTo(),
                    model.getAllocationStatus(),
                    model.getIspcNumber(),
                    model.getApplicationId()
            );
        } else {
            String sql = "INSERT INTO ReportIspcNumber (IspcNumber, Allotee, CreatedBy, CreatedDate, Quantity, AvailableCount," +
                    "DateAllocated, AllocationValidityFrom, AllocationValidityTo, CompanyAllocatedTo, AllocationStatus, ApplicationId) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
            return jdbcTemplate.update(sql,
                    model.getIspcNumber(),
                    model.getAllotee(),
                    model.getCreatedBy(),
                    CurrentTimeStamp.getCurrentTimeStamp(),
                    "1",
                    "0",
                    model.getDateAllocated(),
                    model.getAllocationValidityFrom(),
                    model.getAllocationValidityTo(),
                    model.getCompanyAllocatedTo(),
                    model.getAllocationStatus(),
                    model.getApplicationId()
            );
        }
    }

    /**
     * @param organisation
     * @return
     * @throws SQLException
     */
    public List<ReportIspcNoModel> getByOrganisation(String organisation) throws SQLException {
        String org = (organisation != null ? organisation.trim().toUpperCase() : "");
        String sql = "SELECT * FROM ReportIspcNumber WHERE UPPER(CompanyAllocatedTo)=? ORDER BY CreatedDate DESC";
        List<ReportIspcNoModel> createIspcNoModelsList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ReportIspcNoModel.class), org);
        return createIspcNoModelsList;
    }

    /**
     * @return
     * @throws SQLException
     */
    public List<ReportIspcNoModel> getAll() throws SQLException {
        String sql = "SELECT * FROM ReportIspcNumber ORDER BY CreatedDate DESC";
        List<ReportIspcNoModel> createIspcNoModelsList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ReportIspcNoModel.class));
        return createIspcNoModelsList;
    }


    /**
     * @param model
     * @return
     * @throws SQLException
     */
    public int createSingleNumber(ReportIspcNoModel model) throws SQLException {
        String sql = "INSERT INTO ReportIspcNumber (IspcNumber, Allotee, CreatedBy, CreatedDate, Quantity, AvailableCount, AllocationStatus) " +
                "VALUES(?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                model.getIspcNumber(),
                model.getAllotee(),
                model.getCreatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                "1",
                "0",
                "ALLOCATED"
        );
    }


    /**
     * @param bulkUploadRequest
     * @return
     * @throws SQLException
     */
    public BulkUploadResponse createBulkNumber(BulkUploadReportIspc bulkUploadRequest) throws SQLException {
        log.info("Bulk insert");
        BulkUploadResponse bulkUploadResponse = new BulkUploadResponse();
        List<BulkUploadItem> bulkUploadItem = new ArrayList<>();
        bulkUploadRequest.getBulkList().forEach(bulkItem -> {
            ReportIspcNoModel model = new ReportIspcNoModel();
            BulkUploadItem item = new BulkUploadItem();
            model.setIspcNumber(bulkItem.getIspcNumber());
            model.setAllotee(bulkItem.getAllotee());
            model.setCreatedBy(bulkUploadRequest.getBatchId());
            try {
                int count = countIfNoExist(model.getIspcNumber());
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
     * @param startDate
     * @param endDate
     * @param organisation
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    public List<ReportIspcNoModel> filterNumber(String startDate, String endDate, String organisation, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from ReportIspcNumber WHERE ");

        if (organisation != null && !organisation.isEmpty() &&
                !Objects.equals(organisation, "")
                && !Objects.equals(organisation.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(CompanyAllocatedTo) = '").append("" + organisation.toUpperCase() + "").append("' AND ");
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

        List<ReportIspcNoModel> createIspcNoModelsList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ReportIspcNoModel.class));
        log.info("ReportIspcNoModel {} ", createIspcNoModelsList);
        return createIspcNoModelsList;
    }

}
