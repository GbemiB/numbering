package com.molcom.nms.invoice.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.idManagement.IdManagementService;
import com.molcom.nms.invoice.dto.InvoiceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class InvoiceRepoImpl implements InvoiceRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    IdManagementService idManagementService;

    /**
     * @param applicationId
     * @return
     * @throws Exception
     */
    @Override
    public int isInvoiceExist(String applicationId) throws Exception {
        String sql = "SELECT count(*) FROM Invoice WHERE ApplicationId=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, applicationId);
    }

    /**
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public int createInvoice(InvoiceModel model) throws Exception {
        String invoiceNumber = idManagementService.generateInvoiceId();

        try {
            // Invoice should not be updated
            String sql = "INSERT INTO Invoice (NumberType, NumberSubType, ApplicationId, Organization, " +
                    "InvoiceNumber, InvoiceType, PaymentStatus, InitiatorUsername, InitiatorEmail, Amount, " +
                    "Description, Status, ValueDate,ShouldSendToEservices, SendForNumberReporting) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            return jdbcTemplate.update(sql,
                    model.getNumberType(),
                    model.getNumberSubType(),
                    model.getApplicationId(),
                    model.getOrganization(),
                    invoiceNumber,
                    model.getInvoiceType(),
                    "UNPAID",
                    model.getInitiatorUsername(),
                    model.getInitiatorEmail(),
                    model.getAmount(),
                    model.getDescription(),
                    "",
                    CurrentTimeStamp.getCurrentTimeStamp(),
                    model.getShouldSendToEservices(),
                    "TRUE"
            );

        } catch (Exception exe) {
            log.info("Exception occurred in first step save of standard number {}", exe.getMessage());
            return 0;
        }
    }

    /**
     * @param numberType
     * @param numberSubType
     * @param applicationId
     * @param organisation
     * @param initiatorUsername
     * @param initiatorEmail
     * @param description
     * @return
     * @throws Exception
     */
    @Override
    public int persistZeroFeeInvoiceForMDA(String numberType, String numberSubType,
                                           String applicationId, String organisation,
                                           String initiatorUsername, String initiatorEmail,
                                           String description) throws Exception {
        String invoiceNumber = idManagementService.generateInvoiceId();

        String sql = "INSERT INTO Invoice (NumberType, NumberSubType, ApplicationId, Organization, " +
                "InvoiceNumber, InvoiceType, PaymentStatus, InitiatorUsername, InitiatorEmail, Amount, " +
                "Description, Status, ValueDate,ShouldSendToEservices, IsInvoiceResolvedByEservices, SendForNumberReporting) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                numberType,
                numberSubType,
                applicationId,
                organisation,
                invoiceNumber,
                "MDA",
                "PAID",
                initiatorUsername,
                initiatorEmail,
                0,
                description,
                "",
                CurrentTimeStamp.getCurrentTimeStamp(),
                "FALSE",
                "TRUE",
                "TRUE"
        );
    }

    /**
     * @param transactionRefId
     * @param status
     * @return
     * @throws Exception
     */
    @Override
    public int updateStatus(String transactionRefId, String status) throws Exception {
        String sql = "UPDATE Invoice set PaymentStatus=? WHERE TransactionRefId=?";

        return jdbcTemplate.update(sql, status, transactionRefId);
    }

    /**
     * @param eServicesRequestId
     * @param invoiceId
     * @return
     * @throws Exception
     */
    @Override
    public int updateEservicesInvoiceStatus(String eServicesRequestId, String invoiceId) throws Exception {
        String sql = "UPDATE Invoice set EservicesRequestId=?, ShouldSendToEservices='FALSE' WHERE InvoiceId=?";

        return jdbcTemplate.update(sql, eServicesRequestId, invoiceId);
    }

    /**
     * @param invoiceId
     * @return
     * @throws Exception
     */
    @Override
    public InvoiceModel getById(int invoiceId) throws Exception {
        String sql = "SELECT * FROM Invoice WHERE InvoiceId = ?";
        try {
            InvoiceModel invoiceModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(InvoiceModel.class), invoiceId);
            log.info("InvoiceModel {} ", invoiceModel);
            return invoiceModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    /**
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public List<InvoiceModel> getAll(String rowNumber) throws Exception {
        String sql = "SELECT * from Invoice  ORDER BY ValueDate DESC LIMIT " + rowNumber + "";
        List<InvoiceModel> invoiceModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(InvoiceModel.class));
        log.info("InvoiceModel {} ", invoiceModels);
        return invoiceModels;
    }

    /**
     * @param applicationPaymentStatus
     * @param invoiceType
     * @param invoiceNumber
     * @param startDate
     * @param endDate
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public List<InvoiceModel> filterForRegularUser(String applicationPaymentStatus, String invoiceType,
                                                   String invoiceNumber, String startDate,
                                                   String endDate, String rowNumber) throws Exception {


        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from Invoice WHERE ");

        if (applicationPaymentStatus != null
                && !applicationPaymentStatus.isEmpty()
                && !Objects.equals(applicationPaymentStatus, "")
                && !Objects.equals(applicationPaymentStatus.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(PaymentStatus) = '").append("" + applicationPaymentStatus.toUpperCase() + "").append("' AND ");
        }

        if (invoiceType != null && !invoiceType.isEmpty()
                && !Objects.equals(invoiceType, "")
                && !Objects.equals(invoiceType.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(InvoiceType) = '").append("" + invoiceType.toUpperCase() + "").append("' AND ");
        }

        if (invoiceNumber != null && !invoiceNumber.isEmpty()
                && !Objects.equals(invoiceNumber, "")
                && !Objects.equals(invoiceNumber.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(InvoiceNumber) = '").append("" + invoiceNumber.toUpperCase() + "").append("' AND ");
        }

        if (startDate != null && endDate != null
                &&
                !startDate.isEmpty() &&
                !endDate.isEmpty()) {
            sqlBuilder.append("DATE(ValueDate) BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "') AND ");
        }

        sqlBuilder.append("ValueDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY ValueDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        log.info("sql {} ", sql);

        List<InvoiceModel> invoiceModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(InvoiceModel.class));
        log.info("InvoiceModel {} ", invoiceModels);
        return invoiceModels;
    }

    @Override
    public List<InvoiceModel> filterForRegularUser(String companyName, String applicationPaymentStatus, String invoiceType, String invoiceNumber, String startDate, String endDate, String rowNumber) throws Exception {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from Invoice WHERE ");

        if (applicationPaymentStatus != null
                && !applicationPaymentStatus.isEmpty()
                && !Objects.equals(applicationPaymentStatus, "")
                && !Objects.equals(applicationPaymentStatus.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(PaymentStatus) = '").append("" + applicationPaymentStatus.toUpperCase() + "").append("' AND ");
        }

        if (invoiceType != null && !invoiceType.isEmpty()
                && !Objects.equals(invoiceType, "")
                && !Objects.equals(invoiceType.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(InvoiceType) = '").append("" + invoiceType.toUpperCase() + "").append("' AND ");
        }

        if (invoiceNumber != null && !invoiceNumber.isEmpty()
                && !Objects.equals(invoiceNumber, "")
                && !Objects.equals(invoiceNumber.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(InvoiceNumber) = '").append("" + invoiceNumber.toUpperCase() + "").append("' AND ");
        }

        if (startDate != null && endDate != null
                &&
                !startDate.isEmpty() &&
                !endDate.isEmpty()) {
            sqlBuilder.append("DATE(ValueDate) BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "') AND ");
        }

        sqlBuilder.append("ValueDate IS NOT NULL AND UPPER(Organization)='");
        sqlBuilder.append("" + companyName.toUpperCase() + "").append("' ");
        sqlBuilder.append("ORDER BY ValueDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        log.info("sql {} ", sql);

        List<InvoiceModel> invoiceModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(InvoiceModel.class));
        log.info("InvoiceModel {} ", invoiceModels);
        return invoiceModels;
    }

    /**
     * @param applicationPaymentStatus
     * @param invoiceType
     * @param invoiceNumber
     * @param organization
     * @param startDate
     * @param endDate
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public List<InvoiceModel> filterForAdminUser(String applicationPaymentStatus, String invoiceType,
                                                 String invoiceNumber, String organization,
                                                 String startDate, String endDate, String rowNumber) throws Exception {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from Invoice WHERE ");

        if (applicationPaymentStatus != null
                && !applicationPaymentStatus.isEmpty()
                && !Objects.equals(applicationPaymentStatus, "")
                && !Objects.equals(applicationPaymentStatus.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(PaymentStatus) = '").append("" + applicationPaymentStatus.toUpperCase() + "").append("' AND ");
        }

        if (invoiceType != null
                && !invoiceType.isEmpty()
                && !Objects.equals(invoiceType, "")
                && !Objects.equals(invoiceType.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(InvoiceType) = '").append("" + invoiceType.toUpperCase() + "").append("' AND ");
        }

        if (invoiceNumber != null
                && !invoiceNumber.isEmpty()
                && !Objects.equals(invoiceNumber, "")
                && !Objects.equals(invoiceNumber.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(InvoiceNumber) = '").append("" + invoiceNumber.toUpperCase() + "").append("' AND ");
        }

        if (organization != null
                && !organization.isEmpty()
                && !Objects.equals(organization, "")
                && !Objects.equals(organization.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(Organization) = '").append("" + organization.toUpperCase() + "").append("' AND ");
        }

        if (startDate != null && endDate != null &&
                !startDate.isEmpty() &&
                !endDate.isEmpty()) {
            sqlBuilder.append("DATE(ValueDate) BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "') AND ");
        }

        sqlBuilder.append("ValueDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY ValueDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        log.info("sql {} ", sql);
        List<InvoiceModel> invoiceModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(InvoiceModel.class));
        log.info("InvoiceModel {} ", invoiceModels);
        return invoiceModels;
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public List<InvoiceModel> getListOfInvoiceToBeSentToEservices() throws Exception {
        String sql = "SELECT * from Invoice WHERE ShouldSendToEservices='TRUE'";
        List<InvoiceModel> invoiceModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(InvoiceModel.class));
        return invoiceModels;
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public List<InvoiceModel> getListOfInvoiceNotResolvedEservices() throws Exception {
        String sql = "SELECT * from Invoice WHERE IsInvoiceResolvedByEservices='FALSE'";
        List<InvoiceModel> invoiceModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(InvoiceModel.class));
        return invoiceModels;
    }

    /**
     * @param applicationId
     * @return
     * @throws Exception
     */
    @Override
    public List<InvoiceModel> getInvoiceForRenewal(String applicationId) throws Exception {
        String sql = "SELECT * from Invoice WHERE ApplicationId=? AND InvoiceType='RENEWAL' ";
        List<InvoiceModel> invoiceModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(InvoiceModel.class), applicationId);
        return invoiceModels;
    }

    /**
     * @param applicationId
     * @return
     * @throws Exception
     */
    @Override
    public List<InvoiceModel> getAllocationInvoiceByApplicationId(String applicationId) throws Exception {
        String sql = "SELECT * from Invoice WHERE ApplicationId=? AND InvoiceType='NEW' ";
        List<InvoiceModel> invoiceModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(InvoiceModel.class), applicationId);
        log.info("InvoiceModel {} ", invoiceModels);
        return invoiceModels;
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public List<InvoiceModel> getAllInvoiceNeededForReporting() throws Exception {
        String sql = "SELECT * from Invoice WHERE SendForNumberReporting='TRUE' ";
        List<InvoiceModel> invoiceModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(InvoiceModel.class));
        log.info("InvoiceModel {} ", invoiceModels);
        return invoiceModels;
    }

    /**
     * @param applicationId
     * @return
     */
    @Override
    public List<InvoiceModel> getAllocationInvoice(String applicationId) {
        String sql = "SELECT * from Invoice WHERE ApplicationId=? AND InvoiceType != 'APPLICATION'";
        List<InvoiceModel> invoiceModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(InvoiceModel.class), applicationId);
        log.info("InvoiceModel {} ", invoiceModels);
        return invoiceModels;
    }
}
