package com.molcom.nms.fee.type.repository;

import com.molcom.nms.fee.type.dto.FeeTypeModel;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class FeeTypeRepository implements IFeeTypeRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Data layer to find if feeName already exist
     *
     * @param feeTypeName
     * @return
     * @throws SQLException
     */
    @Override
    public int feeTypeCount(String feeTypeName) throws SQLException {
        String sql = "SELECT count(*) FROM FeeType WHERE FeeTypeName=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, feeTypeName);
    }

    /**
     * Data layer to save new fee type
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int save(FeeTypeModel model) throws SQLException {
        String sql = "INSERT INTO FeeType (FeeTypeName, CreatedUser, CreatedDate) " +
                "VALUES(?,?,?)";
        return jdbcTemplate.update(sql,
                model.getFeeTypeName(),
                model.getCreatedUser(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * Data layer to delete existing fee type by fee id
     *
     * @param feeTypeId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteById(int feeTypeId) throws SQLException {
        String sql = "DELETE FROM FeeType WHERE FeeTypeId = ?";
        return jdbcTemplate.update(sql, feeTypeId);
    }


    /**
     * Data layer to find existing fee type by fee id
     *
     * @param feeTypeId
     * @return
     * @throws SQLException
     */
    @Override
    public FeeTypeModel findById(int feeTypeId) throws SQLException {
        String sql = "SELECT * FROM FeeType WHERE FeeTypeId = ?";
        try {
            FeeTypeModel feeTypeModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(FeeTypeModel.class), feeTypeId);
            log.info("FeeTypeModel {} ", feeTypeModel);
            return feeTypeModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    /**
     * Data layer to get all fee types
     *
     * @return
     * @throws SQLException
     */
    @Override
    public List<FeeTypeModel> getAll() throws SQLException {
        String sql = "SELECT * from FeeType ORDER BY FeeTypeName ASC";
        List<FeeTypeModel> feeTypeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(FeeTypeModel.class));
        return feeTypeModels;

    }

    /**
     * Data layer to filterForRegularUser existing fee type by FeeName
     *
     * @param queryParam
     * @param queryValue
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<FeeTypeModel> filter(String queryParam, String queryValue, String rowNumber) throws SQLException {
        String sql = "SELECT * from FeeType WHERE  " + queryParam + "  LIKE '%" + queryValue + "%' " + "ORDER BY FeeTypeName ASC LIMIT " + rowNumber + "";
        List<FeeTypeModel> feeTypeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(FeeTypeModel.class));
        return feeTypeModels;
    }
}
