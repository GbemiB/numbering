package com.molcom.nms.number.selectedNumber.repository;

import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableCellGetRepository;
import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableUpdateRepository;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.number.selectedNumber.dto.DistinctNumber;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
import com.molcom.nms.numbertype.repository.INumberTypeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class SelectedNumbersRepo implements ISelectedNumbersRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GenericTableUpdateRepository genericTableUpdateRepository;

    @Autowired
    private INumberTypeRepo numberTypeRepo;

    @Autowired
    private GenericTableCellGetRepository genericTableCellGetRepository;

    public int countPerApplicationId(String applicationId) throws SQLException {
        String sql = "SELECT count(*) FROM SelectedNumbers WHERE ApplicationId=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, applicationId);
    }

    public int saveSelectedNumber(SelectedNumberModel model) throws SQLException {
        String subType = (model.getNumberSubType() != null ? model.getNumberSubType() : "");

        // Update number to `IsSelected` so it won't be available for another user
        int isUpdated = genericTableUpdateRepository.updateTableColumn("NumberBlock",
                "IsSelected",
                "TRUE",
                "NumberBlock",
                model.getSelectedNumberValue()
        );
        log.info("Number {}  is not longer available for selection status {} ", model.getSelectedNumberValue(), isUpdated);

        // If ISPC number, update available count as 0 since ISPC number does not have number block
        if (subType.equalsIgnoreCase("ispc")) {
            int isISPCUpdated = genericTableUpdateRepository.updateTableColumn("CreatedIspcNumber",
                    "AvailableCount",
                    "0",
                    "IspcNumber",
                    model.getSelectedNumberValue()
            );
            log.info("Update of current ISPC number {} available count as 0 status {} ", model.getSelectedNumberValue(), isUpdated);
        }


        String sql3 = "INSERT INTO SelectedNumbers (SelectedNumberValue, ApplicationId, Purpose," +
                " BearerMedium, Tariff, NumberType, NumberSubType, CreatedDate) " +
                "VALUES(?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql3,
                model.getSelectedNumberValue(),
                model.getApplicationId(),
                model.getPurpose(),
                model.getBearerMedium(),
                model.getTariff(),
                model.getNumberType(),
                model.getNumberSubType(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteByAppId(String applicationId) throws SQLException {
        String sql = "DELETE FROM SelectedNumbers WHERE ApplicationId=?";
        return jdbcTemplate.update(sql, applicationId);
    }

    /**
     * Data layer to delete existing selected number
     *
     * @param selectedNumberId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteSelectedNumber(String selectedNumberId) throws SQLException {
        String sql = "DELETE FROM SelectedNumbers WHERE SelectedNumberId = ?";
        return jdbcTemplate.update(sql, selectedNumberId);
    }

    /**
     * Data layer to get existing selected number by application id
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<SelectedNumberModel> getSelectedNumber(String applicationId) throws SQLException {
        String sql = "SELECT * FROM SelectedNumbers WHERE ApplicationId = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SelectedNumberModel.class), applicationId);
    }

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
    public int updateAfterAllocation(String applicationId, String companyAllocatedTo) throws SQLException {
        String billingCycle = "";
        Integer billingDays = 356;

        String numberSubType = genericTableCellGetRepository.getNumberSubType(applicationId);
        String numberType = genericTableCellGetRepository.getNumberType(applicationId);
        String shortCodeCategory = genericTableCellGetRepository.getShortCodeCat(applicationId);

//        if (numberSubType != null ){
//            if ((Objects.equals(numberType.toUpperCase(), "STANDARD")) ||
//                    (Objects.equals(numberType.toUpperCase(), "SPECIAL"))  ||
//                    (Objects.equals(numberType.toUpperCase(), "ISPC")) ){
//
//                List<NumberTypeModel> numberTypeModels = numberTypeRepo.getByNumberSubType(numberSubType);
//
//                if(numberTypeModels.size() >= 1){
//                    NumberTypeModel numberTypeModel = numberTypeModels.get(0);
//                    billingCycle = numberTypeModel.getBillingFrequency();
//                    billingDays = determineBillingDays(billingCycle);
//
//                }
//            }
//
//            if (Objects.equals(numberType.toUpperCase(), "SHORT-CODE")){
//                List<NumberTypeModel> numberTypeModels = numberTypeRepo.getForShortCodeType(shortCodeCategory);
//                log.info("Short code number type {}", numberTypeModels);
//                if(numberTypeModels.size() >= 1){
//                    NumberTypeModel numberTypeModel = numberTypeModels.get(0);
//                    billingCycle = numberTypeModel.getBillingFrequency();
//                    billingDays = determineBillingDays(billingCycle);
//                }
//            }
//        }


        Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
        String sql2 = "UPDATE SelectedNumbers SET IsNumberAllocated=?, AllocationStatus =?, DateAllocated=?, " +
                "AllocationValidityFrom=?, AllocationValidityTo=?, CompanyAllocatedTo=? WHERE ApplicationId =?";
        return jdbcTemplate.update(sql2,
                "YES",
                "ALLOCATED",
                todayDate,
                todayDate,
//                CurrentTimeStamp.getNextYearTimeStamp(todayDate, billingDays),
                CurrentTimeStamp.getNextYearTimeStamp(todayDate, 365),
                companyAllocatedTo,
                applicationId
        );
    }

    //    ProcessForRenewal
    @Override
    public int updateApplicationForRenewal() throws SQLException {
        String sql2 = "UPDATE SelectedNumbers SET ProcessForRenewal ='TRUE'" +
                " WHERE DATE(AllocationValidityTo) < CURRENT_DATE AND IsRenewalResolved='FALSE'";
        return jdbcTemplate.update(sql2);
    }

    @Override
    public int manuallyExpireApplication(String applicationId) throws SQLException {
        Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
        String sql2 = "UPDATE SelectedNumbers SET AllocationValidityTo=? WHERE ApplicationId =?";
        return jdbcTemplate.update(sql2,
                // add less than 3 days to validity period so that job picking expired application can pick it
                CurrentTimeStamp.getNextYearTimeStamp(todayDate, -3),
                applicationId
        );
    }

    @Override
    public int updateApplicationNotToBePickedRenewal(String applicationId) throws SQLException {
        String sql2 = "UPDATE SelectedNumbers SET ProcessForRenewal ='FALSE', IsRenewalResolved='TRUE' WHERE ApplicationId=?";
        return jdbcTemplate.update(sql2, applicationId);
    }


    /**
     * @return
     * @throws SQLException
     */
    public List<DistinctNumber> getExpiredDistinct() throws SQLException {
        String sql = "SELECT DISTINCT ApplicationId, NumberSubType, NumberType FROM SelectedNumbers WHERE ProcessForRenewal ='TRUE'";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(DistinctNumber.class));
    }


    @Override
    public List<SelectedNumberModel> getRejectedNumbers(String applicationId) throws SQLException {
        String sql = "SELECT * FROM SelectedNumbers WHERE ApplicationId=? AND IsNoRejected='TRUE'";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SelectedNumberModel.class), applicationId);
    }

    @Override
    public int dropRejectedNumbers(String applicationId) throws SQLException {
        String sql = "DELETE FROM SelectedNumbers WHERE ApplicationId=? AND IsNoRejected='TRUE'";
        return jdbcTemplate.update(sql, applicationId);
    }
}
