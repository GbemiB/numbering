package com.molcom.nms.numberReport.standard.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.invoice.dto.InvoiceModel;
import com.molcom.nms.invoice.repository.InvoiceRepository;
import com.molcom.nms.numberReport.standard.dto.BulkUploadItem;
import com.molcom.nms.numberReport.standard.dto.BulkUploadReportStandard;
import com.molcom.nms.numberReport.standard.dto.BulkUploadResponse;
import com.molcom.nms.numberReport.standard.dto.ReportStandardNoModel;
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
public class ReportStandardNoRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public int countIfNoExist(String minNumber, String maxNumber, String numberSubType, String applicationId) throws SQLException {
        String sql = "SELECT count(*) FROM ReportStandardNumber WHERE MinimumNumber=? AND MaximumNumber=? AND NumberSubType=? AND ApplicationId=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, minNumber, maxNumber, numberSubType, applicationId);
    }


    public int updateAfterAllocation(String applicationId) throws SQLException {
        Timestamp now = CurrentTimeStamp.getCurrentTimeStamp();
        Timestamp next = CurrentTimeStamp.getNextYearTimeStamp(now, 365);
        // means the number has been reported already
        String sql2 = "UPDATE ReportStandardNumber SET DateAllocated=?, AllocationValidityFrom=?," +
                "AllocationValidityTo=?, AllocationStatus=? WHERE ApplicationId=?";
        return jdbcTemplate.update(sql2,
                now,
                now,
                next,
                "ALLOCATED",
                applicationId
        );

    }


    public int internalReport(ReportStandardNoModel model) throws SQLException {
        int count = countIfNoExist(model.getMinimumNumber(), model.getMaximumNumber(), model.getNumberSubType(), model.getApplicationId());

        if (count >= 1) {
            // means the number has been reported already
            String sql2 = "UPDATE ReportStandardNumber SET DateAllocated =?, AllocationValidityFrom =?," +
                    "AllocationValidityTo =?, CompanyAllocatedTo =?, AllocationStatus=?, Purpose=?,BearerMedium=?  WHERE MinimumNumber=? AND MaximumNumber=?" +
                    "AND NumberSubType=? AND ApplicationId=?";
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
            log.info("bbb ====>>>>>");
            String time = String.valueOf(CurrentTimeStamp.getCurrentTimeStamp());
            String sql = "INSERT INTO ReportStandardNumber (NumberType, NumberSubType,CoverageArea, AreaCode, " +
                    "AccessCode,MinimumNumber,MaximumNumber," +
                    "CreatedBy, CreatedDate,Quantity, Allotee, AvailableCount," +
                    "DateAllocated, AllocationValidityFrom, " +
                    "AllocationValidityTo, CompanyAllocatedTo, AllocationStatus, ApplicationId, Purpose, BearerMedium) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            return jdbcTemplate.update(sql,
                    "Standard",
                    model.getNumberSubType(),
                    model.getCoverageArea(),
                    model.getAreaCode(),
                    model.getAccessCode(),
                    model.getMinimumNumber(),
                    model.getMaximumNumber(),
                    model.getCreatedBy(),
                    time,
                    model.getQuantity(),
                    model.getAllotee(),
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

    public int createSingleNumber(ReportStandardNoModel model) throws SQLException {
        String qty = "";
        if (Objects.equals(model.getNumberSubType(), "NATIONAL")) {
            qty = "1000000";
        }
        if (Objects.equals(model.getNumberSubType(), "GEOGRAPHICAL")) {
            qty = "10000";
        }

        String time = String.valueOf(CurrentTimeStamp.getCurrentTimeStamp());
        String sql = "INSERT INTO ReportStandardNumber (NumberType, NumberSubType,CoverageArea, AreaCode, AccessCode,MinimumNumber,MaximumNumber," +
                "CreatedBy, CreatedDate,Quantity, Allotee, AvailableCount, AllocationStatus ) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                "Standard",
                model.getNumberSubType(),
                model.getCoverageArea(),
                model.getAreaCode(),
                model.getAccessCode(),
                model.getMinimumNumber(),
                model.getMaximumNumber(),
                model.getCreatedBy(),
                time,
                qty,
                model.getAllotee(),
                "0",
                "ALLOCATED"
        );
    }

    public BulkUploadResponse createBulkNumber(BulkUploadReportStandard bulkUploadRequest) throws SQLException {
        log.info("Bulk insert");
        BulkUploadResponse bulkUploadResponse = new BulkUploadResponse();
        List<BulkUploadItem> bulkUploadItem = new ArrayList<>();
        bulkUploadRequest.getBulkList().forEach(bulkItem -> {
            ReportStandardNoModel model = new ReportStandardNoModel();
            BulkUploadItem item = new BulkUploadItem();
            model.setNumberType("Standard");
            model.setNumberSubType(bulkItem.getNumberSubType());
            model.setAreaCode(bulkItem.getAreaCode());
            model.setAccessCode(bulkItem.getAccessCode());
            model.setMinimumNumber(bulkItem.getSubBlockStart());
            model.setMaximumNumber(bulkItem.getSubBlockEnd());
            model.setCreatedBy(bulkItem.getAllotee());
            model.setCreatedDate("");
            model.setQuantity("");
            model.setAllotee(bulkItem.getAllotee());
            model.setDateAllocated("");
            model.setAllocationValidityFrom("");
            model.setCreatedBy(bulkUploadRequest.getBatchId());

            try {
                int count = countIfNoExist(model.getMinimumNumber(), model.getMinimumNumber(), model.getNumberSubType(), model.getApplicationId());
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


    public List<ReportStandardNoModel> filterNumber(String organisation, String numberSubType, String coverageArea, String areaCode, String accessCode, String startDate, String endDate, String rowNumber) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from ReportStandardNumber WHERE ");

        if (numberSubType != null && !numberSubType.isEmpty() &&
                !Objects.equals(numberSubType, "")
                && !Objects.equals(numberSubType.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(NumberSubType) = '").append("" + numberSubType.toUpperCase() + "").append("' AND ");
        }

        if (organisation != null && !organisation.isEmpty() &&
                !Objects.equals(organisation, "")
                && !Objects.equals(organisation.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(CompanyAllocatedTo) = '").append("" + organisation.toUpperCase() + "").append("' AND ");
        }

        if (coverageArea != null && !coverageArea.isEmpty() &&
                !Objects.equals(coverageArea, "")
                && !Objects.equals(coverageArea.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(CoverageArea) = '").append("" + coverageArea.toUpperCase() + "").append("' AND ");
        }

        if (areaCode != null && !areaCode.isEmpty() &&
                !Objects.equals(areaCode, "")
                && !Objects.equals(areaCode.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(AreaCode) = '").append("" + areaCode.toUpperCase() + "").append("' AND ");
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

        List<ReportStandardNoModel> CreateSpecialNoModelsList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ReportStandardNoModel.class));
        log.info("ReportShortCodeNoModel {} ", CreateSpecialNoModelsList);
        return CreateSpecialNoModelsList;
    }

    /**
     * @param organisation
     * @return
     * @throws SQLException
     */
    public List<ReportStandardNoModel> getByOrganisation(String organisation) throws SQLException {
        String org = (organisation != null ? organisation.trim().toUpperCase() : "");
        String sql = "SELECT * FROM ReportStandardNumber WHERE UPPER(CompanyAllocatedTo)=? ORDER BY CreatedDate DESC";
        List<ReportStandardNoModel> models = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ReportStandardNoModel.class), org);
        return models;
    }

    /**
     * @return
     * @throws SQLException
     */
    public List<ReportStandardNoModel> getAll() throws SQLException {
        String sql = "SELECT * FROM ReportStandardNumber ORDER BY CreatedDate DESC";
        List<ReportStandardNoModel> modelList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ReportStandardNoModel.class));
        return modelList;
    }

    public void updateAllocationStatus() throws SQLException {
        // Get list of ongoing allocation reports
        String sql1 = "SELECT * FROM ReportStandardNumber WHERE UPPER(AllocationStatus)='ONGOING ALLOCATION'";
        List<ReportStandardNoModel> modelList = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(ReportStandardNoModel.class));
        log.info("Ongoing standard allocation status update {} ", modelList);
        modelList.forEach(mod -> {
            log.info("Current item been worked on {} ", mod);
            try {
                List<InvoiceModel> list = invoiceRepository.getAllocationInvoice(mod.getApplicationId());
                // If status is PAID, update allocation status to allocated
                if (list != null && list.size() >= 1 && Objects.equals(list.get(0).getPaymentStatus(), "PAID")) {
                    String sql2 = "UPDATE ReportStandardNumber SET AllocationStatus='ALLOCATED' WHERE ApplicationId=?";
                    int statusUpdated = jdbcTemplate.update(sql2,
                            mod.getApplicationId());
                    log.info("sql {} ", sql2);
                    log.info("Update of allocation status {} ", statusUpdated);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

}
