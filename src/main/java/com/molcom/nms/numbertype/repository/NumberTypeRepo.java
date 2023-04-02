package com.molcom.nms.numbertype.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.numbertype.dto.NumberTypeModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class NumberTypeRepo implements INumberTypeRepo {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @param model
     * @param numberTypeId
     * @return
     * @throws SQLException
     */
    @Override
    public int update(NumberTypeModel model, int numberTypeId) throws SQLException {
        String sql = "UPDATE NumberType set BillingFrequency = ?, UpdatedBy = ? WHERE numberTypeId = ?";
        return jdbcTemplate.update(sql,
                model.getBillingFrequency(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                numberTypeId
        );
    }

    /**
     * @param numberTypeId
     * @return
     * @throws SQLException
     */
    @Override
    public NumberTypeModel findById(int numberTypeId) throws SQLException {
        String sql = "SELECT * FROM NumberType WHERE NumberTypeId = ?";
        try {
            NumberTypeModel numberTypeModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(NumberTypeModel.class), numberTypeId);
            log.info("NumberTypeModelBody {} ", numberTypeModel);
            return numberTypeModel;
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
    public List<NumberTypeModel> getAll() throws SQLException {
        String sql = "SELECT * from NumberType";
        List<NumberTypeModel> numberTypeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(NumberTypeModel.class));
        log.info("NumberTypeModelBody {} ", numberTypeModels);
        return numberTypeModels;
    }

    /**
     * @param numberSubType
     * @return
     * @throws SQLException
     */
    @Override
    public List<NumberTypeModel> getByNumberSubType(String numberSubType) throws SQLException {
        String subType = (numberSubType != null ? numberSubType.toUpperCase() : "");
        String sql = "SELECT * from NumberType WHERE UPPER(NumberSubType)=?";
        List<NumberTypeModel> numberTypeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(NumberTypeModel.class), subType);
        log.info("NumberTypeModelBody {} ", numberTypeModels);
        return numberTypeModels;
    }

    @Override
    public List<NumberTypeModel> getForShortCodeType(String shortCodeType) throws SQLException {
        String type = (shortCodeType != null ? shortCodeType.toUpperCase() : "");
        String sql = "SELECT * from NumberType WHERE UPPER(NumberSubType) LIKE '%" + type + "%'";
        List<NumberTypeModel> numberTypeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(NumberTypeModel.class), shortCodeType);
        log.info("NumberTypeModelBody {} ", numberTypeModels);
        return numberTypeModels;
    }


    /**
     * @param queryParam
     * @param queryValue
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<NumberTypeModel> filter(String queryParam, String queryValue, String rowNumber) throws SQLException {
        String sql = "SELECT * from NumberType WHERE  " + queryParam + "  LIKE '%" + queryValue + "%' " + "ORDER BY " + queryParam + " ASC LIMIT " + rowNumber + "";
        List<NumberTypeModel> numberTypeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(NumberTypeModel.class));
        log.info("NumberTypeModelBody {} ", numberTypeModels);
        return numberTypeModels;
    }
}
