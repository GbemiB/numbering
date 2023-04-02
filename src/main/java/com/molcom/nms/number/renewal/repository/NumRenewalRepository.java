package com.molcom.nms.number.renewal.repository;

import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableCellGetRepository;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.number.renewal.dto.NumRenewalModel;
import com.molcom.nms.numbertype.repository.INumberTypeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class NumRenewalRepository implements INumRenewalRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GenericTableCellGetRepository genericTableCellGetRepository;

    @Autowired
    private INumberTypeRepo numberTypeRepo;

    /**
     * Determine billing cycle
     *
     * @param billingCycle
     * @return
     */
    private Integer determineBillingDays(String billingCycle) {
        Integer days = 0;
        if (billingCycle != null && Objects.equals(billingCycle.toUpperCase(), "YEARLY")) {
            days = 365;
        }
        if (billingCycle != null && Objects.equals(billingCycle.toUpperCase(), "EVERY YEARLY")) {
            days = 365;
        }
        if (billingCycle != null && Objects.equals(billingCycle.toUpperCase(), "EVERY MONTH")) {
            days = 31;
        }
        if (billingCycle != null && Objects.equals(billingCycle.toUpperCase(), " MONTHLY")) {
            days = 31;
        }
        return days;
    }


    @Override
    public int updateAfterRenewal(String applicationId, String companyAllocatedTo) throws SQLException {
        String billingCycle = "";
        Integer billingDays = 356;

        String numberSubType = genericTableCellGetRepository.getNumberSubType(applicationId);
        String numberType = genericTableCellGetRepository.getNumberType(applicationId);
        String shortCodeCategory = genericTableCellGetRepository.getShortCodeCat(applicationId);

        Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
        String sql2 = "UPDATE NumberRenewal SET StartBillingPeriod=?, EndBillingPeriod =?, DateAllocatedFrom=?, " +
                "DateAllocatedTo=?, ApplicationExpiryDate=?, Organization=? WHERE ApplicationId =?";
        return jdbcTemplate.update(sql2,

                todayDate,
                CurrentTimeStamp.getNextYearTimeStamp(todayDate, 365),
                todayDate,
                CurrentTimeStamp.getNextYearTimeStamp(todayDate, 365),
                CurrentTimeStamp.getNextYearTimeStamp(todayDate, 365),
                companyAllocatedTo,
                applicationId

        );
    }


    /**
     * @param appId
     * @return
     * @throws SQLException
     */
    @Override
    public int countIfNoExist(String appId) throws SQLException {
        String sql = "SELECT count(*) FROM NumberRenewal WHERE ApplicationId=? ";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, appId);
    }


    /**
     * @param model
     * @return
     */
    @Override
    public int save(NumRenewalModel model) {
        try {
            int count = countIfNoExist(model.getApplicationId());
            if (count == 0) {
                Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
                int isRenewalUpdated = updateAfterRenewal(model.getApplicationId(), model.getOrganization());
                log.info("Is renewal updated {}", isRenewalUpdated);

                String sql = "INSERT INTO NumberRenewal (ApplicationId, Organization, StartBillingPeriod, " +
                        "EndBillingPeriod, RenewalStatus, PaymentStatus, ApplicationExpiryDate, DateAllocatedFrom," +
                        "DateAllocatedTo, NumberType, NumberSubType) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
                return jdbcTemplate.update(sql,
                        model.getApplicationId(),
                        model.getOrganization(),
                        todayDate,
                        CurrentTimeStamp.getNextYearTimeStamp(todayDate, 365),
                        model.getRenewalStatus(),
                        model.getPaymentStatus(),
                        CurrentTimeStamp.getNextYearTimeStamp(todayDate, 365),
                        todayDate,
                        CurrentTimeStamp.getNextYearTimeStamp(todayDate, 365),
                        model.getNumberType(),
                        model.getNumberSubType()
                );
            } else return -1;
        } catch (Exception exe) {
            log.info("Exception !!! {}", exe.getMessage());
            return 0;
        }
    }

    /**
     * Data layer to find billing record by application id
     *
     * @param renewalId Unique field of a billing
     * @return NumRenewalModel
     * @throws SQLException
     */
    @Override
    public NumRenewalModel getByRenewalId(int renewalId) throws SQLException {
        String sql = "SELECT * FROM NumberRenewal WHERE RenewalId=?";
        try {
            NumRenewalModel numRenewalModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(NumRenewalModel.class), renewalId);
            log.info("NumRenewalModel {} ", numRenewalModel);
            return numRenewalModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred while number getting renewal details by billing id ");
            return null;
        }
    }

    @Override
    public NumRenewalModel getByApplicationId(String applicationId) throws SQLException {
        String sql = "SELECT * FROM NumberRenewal WHERE ApplicationId=?";
        try {
            NumRenewalModel numRenewalModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(NumRenewalModel.class), applicationId);
            log.info("NumRenewalModel {} ", numRenewalModel);
            return numRenewalModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception !!!!! ");
            return null;
        }
    }

    /**
     * @return
     * @throws SQLException
     */
    @Override
    public List<NumRenewalModel> getAll() throws SQLException {
        String sql = "SELECT * from NumberRenewal  ORDER BY DateAllocatedTo DESC";
        List<NumRenewalModel> numRenewalModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(NumRenewalModel.class));
        log.info("NumRenewalModel {} ", numRenewalModels);
        return numRenewalModels;
    }

    /**
     * Data layer to filter for regular user
     *
     * @return Result set of List<NumRenewalModel>
     * @throws Exception
     */
    @Override
    public List<NumRenewalModel> filterForRegularUser(String queryParam1, String queryValue1,
                                                      String queryParam2, String queryValue2,
                                                      String queryParam3, String queryValue3,
                                                      String queryParam4, String queryValue4,
                                                      String queryParam5, String queryValue5,
                                                      String companyName,
                                                      String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from NumberRenewal WHERE ");

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

        if (queryValue5 != null && !queryValue5.isEmpty() &&
                !Objects.equals(queryValue5, "")
                && !Objects.equals(queryValue5.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue5.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam5 + " = '").append("" + queryValue5 + "").append("' AND ");
        }

//        sqlBuilder.append("ApplicationId IS NOT NULL ");
//        sqlBuilder.append("ORDER BY DateAllocatedFrom DESC LIMIT ").append(rowNumber);
//        String sql = sqlBuilder.toString();

        sqlBuilder.append("ApplicationId IS NOT NULL AND UPPER(Organization)='");
        sqlBuilder.append("" + companyName.toUpperCase() + "").append("' ");
        sqlBuilder.append("ORDER BY DateAllocatedFrom DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        log.info("SQL query {} ", sql);
        List<NumRenewalModel> numRenewalModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(NumRenewalModel.class));
        log.info("NumRenewalModel {} ", numRenewalModels);
        return numRenewalModels;
    }

    /**
     * Data layer to filter for admin user
     *
     * @return Result set of List<NumRenewalModel>
     * @return
     * @throws SQLException
     */
    @Override
    public List<NumRenewalModel> filterForAdminUser(String queryParam1, String queryValue1,
                                                    String queryParam2, String queryValue2,
                                                    String queryParam3, String queryValue3,
                                                    String queryParam4, String queryValue4,
                                                    String queryParam5, String queryValue5,
                                                    String queryParam6, String queryValue6,
                                                    String queryParam7, String queryValue7,
                                                    String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from NumberRenewal WHERE ");

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

        if (queryValue5 != null && !queryValue5.isEmpty() &&
                !Objects.equals(queryValue5, "")
                && !Objects.equals(queryValue5.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue5.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam5 + " = '").append("" + queryValue5 + "").append("' AND ");
        }

        if (queryValue6 != null && !queryValue6.isEmpty() &&
                !Objects.equals(queryValue6, "")
                && !Objects.equals(queryValue6.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue6.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam6 + " = '").append("" + queryValue6 + "").append("' AND ");
        }

        if (queryValue7 != null && !queryValue7.isEmpty() &&
                !Objects.equals(queryValue7, "")
                && !Objects.equals(queryValue7.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue7.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam7 + " = '").append("" + queryValue7 + "").append("' AND ");
        }

        sqlBuilder.append("ApplicationId IS NOT NULL ");
        sqlBuilder.append("ORDER BY DateAllocatedFrom DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();


        log.info("SQL query {} ", sql);
        List<NumRenewalModel> numRenewalModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(NumRenewalModel.class));
        log.info("NumRenewalModel {} ", numRenewalModels);
        return numRenewalModels;
    }
}
