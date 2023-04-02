package com.molcom.nms.fee.schedule.repository;

import com.molcom.nms.fee.schedule.dto.FeeScheduleModel;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class FeeScheduleRepo implements IFeeScheduleRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int isExist(FeeScheduleModel model) throws SQLException {
        String a = (model.getFeeType() != null ? model.getFeeType().toUpperCase() : "");
        String b = (model.getNumberType() != null ? model.getNumberType().toUpperCase() : "");
        String c = (model.getNumberSubType() != null ? model.getNumberSubType().toUpperCase() : "");
        String sql = "SELECT count(*) FROM FeeSchedule WHERE UPPER(FeeType) =? AND UPPER(NumberType) =? and UPPER(NumberSubType) =?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, a, b, c);
    }

    /**
     * Datalayer to add new fee schedule
     *
     * @param model
     * @return
     * @throws SQLException
     */

    @Override
    public int save(FeeScheduleModel model) throws SQLException {
        String sql = "INSERT INTO FeeSchedule (FeeType, NumberType, NumberSubType, BillingStage, " +
                "InitialValue, InitialValueType, IsRenewable, RenewableType, RenewableValueType, " +
                "CreatedUser, CreatedDate  ) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                model.getFeeType(),
                model.getNumberType(),
                model.getNumberSubType(),
                model.getBillingStage(),
                model.getInitialValue(),
                model.getInitialValueType(),
                model.getIsRenewable(),
                model.getRenewableType(),
                model.getRenewableValueType(),
                model.getCreatedUser(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * Datalayer to edit existing fee schedule
     *
     * @param model
     * @param feeScheduleId
     * @return
     * @throws SQLException
     */
    @Override
    public int edit(FeeScheduleModel model, int feeScheduleId) throws SQLException {
        String sql = "UPDATE FeeSchedule set InitialValue = ?, RenewableValueType = ?, ModifiedUser  = ?, ModifiedDate = ?" +
                " WHERE FeeScheduleId = ?";

        return jdbcTemplate.update(sql,
                model.getInitialValue(),
                model.getRenewableValueType(),
                model.getModifiedUser(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                feeScheduleId
        );
    }

    /**
     * @param feeScheduleId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteById(int feeScheduleId) throws SQLException {
        String sql = "DELETE FROM FeeSchedule WHERE FeeScheduleId = ?";
        return jdbcTemplate.update(sql, feeScheduleId);
    }

    /**
     * Datalayer to find existing fee schedule by id
     *
     * @param FeeScheduleModel
     * @return
     * @throws SQLException
     */
    @Override
    public FeeScheduleModel findById(int FeeScheduleModel) throws SQLException {
        String sql = "SELECT * FROM FeeSchedule WHERE FeeScheduleId = ?";
        try {
            FeeScheduleModel feeScheduleModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(FeeScheduleModel.class), FeeScheduleModel);
            log.info("FeeScheduleModel {} ", feeScheduleModel);
            return feeScheduleModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    /**
     * @return
     * @throws SQLException
     */
    @Override
    public List<FeeScheduleModel> getShortCodeFee() throws SQLException {
        String sql = "SELECT * from FeeSchedule WHERE UPPER(NumberType)='SHORT-CODE' ORDER BY CreatedDate ASC";
        List<FeeScheduleModel> feeScheduleModelList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(FeeScheduleModel.class));
        log.info("FeeTypeModel {} ", feeScheduleModelList);
        return feeScheduleModelList;
    }

    /**
     * @param feeName
     * @return
     * @throws SQLException
     */
    @Override
    public List<FeeScheduleModel> getShortCodeFeeByFeeName(String feeName) throws SQLException {
        String feeNameVal = (feeName != null ? feeName.toLowerCase() : "");
        String sql = "SELECT * from FeeSchedule WHERE UPPER(NumberType)='SHORT-CODE' AND UPPER(FeeType)=?";
        List<FeeScheduleModel> feeScheduleModelList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(FeeScheduleModel.class), feeNameVal);
        log.info("FeeTypeModel {} ", feeScheduleModelList);
        return feeScheduleModelList;
    }

    /**
     * Datalayer to filterForRegularUser existing fee schedules
     *
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param queryParam3
     * @param queryValue3
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<FeeScheduleModel> filter(String queryParam1, String queryValue1, String queryParam2, String queryValue2, String queryParam3, String queryValue3, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from FeeSchedule WHERE ");

        if (queryValue1 != null && !queryValue1.isEmpty() &&
                !Objects.equals(queryValue1, "")
                && !Objects.equals(queryValue1.toUpperCase(), "SELECT")) {
            sqlBuilder.append("" + queryParam1 + " = '").append("" + queryValue1 + "").append("' AND ");
        }

        if (queryValue2 != null && !queryValue2.isEmpty() &&
                !Objects.equals(queryValue2, "")
                && !Objects.equals(queryValue2.toUpperCase(), "SELECT")) {
            sqlBuilder.append("" + queryParam2 + " = '").append("" + queryValue2 + "").append("' AND ");
        }

        if (queryValue3 != null && !queryValue3.isEmpty() &&
                !Objects.equals(queryValue3, "")
                && !Objects.equals(queryValue3.toUpperCase(), "SELECT")) {
            sqlBuilder.append("" + queryParam3 + " = '").append("" + queryValue3 + "").append("' AND ");
        }

        sqlBuilder.append("CreatedDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        List<FeeScheduleModel> feeScheduleModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(FeeScheduleModel.class));
        log.info("FeeScheduleModel {} ", feeScheduleModels);
        return feeScheduleModels;
    }

    /**
     * @return
     * @throws SQLException
     */
    @Override
    public List<FeeScheduleModel> getAll() throws SQLException {
        String sql = "SELECT * from FeeSchedule ORDER BY FeeType ASC";
        List<FeeScheduleModel> feeScheduleModelList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(FeeScheduleModel.class));
        log.info("FeeTypeModel {} ", feeScheduleModelList);
        return feeScheduleModelList;
    }

    /**
     * @param feeType
     * @param numberType
     * @param numberSubType
     * @return
     * @throws SQLException
     */
    @Override
    public List<FeeScheduleModel> getSpecificFeeSchedule(String feeType, String numberType, String numberSubType) throws SQLException {
        String sql = "SELECT * from FeeSchedule WHERE UPPER(FeeType) = '" + feeType + "' AND UPPER(NumberType) = '" + numberType + "' AND UPPER(NumberSubType ) = '" + numberSubType + "' ";
        List<FeeScheduleModel> feeScheduleModelList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(FeeScheduleModel.class));
        log.info("SQL {} ", sql);
        log.info("FeeTypeModel {} ", feeScheduleModelList);
        return feeScheduleModelList;
    }
}
