package com.molcom.nms.billingcycle.repository;

import com.molcom.nms.billingcycle.dto.BillingCycleModel;
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
public class BillingCycleRepo implements IBillingCycleRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * @param billingName
     * @return
     * @throws SQLException
     */
    @Override
    public int isExist(String billingName) throws SQLException {
        String sql = "SELECT count(*) FROM BillingCycle WHERE BillingName=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, billingName);
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int save(BillingCycleModel model) throws SQLException {
        String sql = "INSERT INTO BillingCycle (BillingName, BillingType, BillingPeriod, BillingCycle, CreatedUser, CreatedDate) " +
                "VALUES(?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                model.getBillingName(),
                model.getBillingType(),
                model.getBillingPeriod(),
                model.getBillingCycle(),
                model.getCreatedUser(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * @param model
     * @param billingId
     * @return
     * @throws SQLException
     */
    @Override
    public int edit(BillingCycleModel model, int billingId) throws SQLException {
        String sql = "UPDATE BillingCycle set BillingName=?, BillingType=?, BillingPeriod=?, BillingCycle=?, UpdatedBy=?, UpdateDate=? WHERE BillingId = ?";

        return jdbcTemplate.update(sql,
                model.getBillingName(),
                model.getBillingType(),
                model.getBillingPeriod(),
                model.getBillingCycle(),
                model.getUpdatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                billingId
        );
    }

    /**
     * @param billingId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteById(int billingId) throws SQLException {
        String sql = "DELETE FROM BillingCycle WHERE BillingId=?";
        return jdbcTemplate.update(sql, billingId);
    }

    /**
     * @param billingId
     * @return
     * @throws SQLException
     */
    @Override
    public BillingCycleModel findById(int billingId) throws SQLException {
        String sql = "SELECT * FROM BillingCycle WHERE BillingId = ?";
        try {
            BillingCycleModel billingCycleModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(BillingCycleModel.class), billingId);
            log.info("BillingCycleModel {} ", billingCycleModel);
            return billingCycleModel;
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
    public List<BillingCycleModel> getAll() throws SQLException {
        String sql = "SELECT * from BillingCycle ORDER BY BillingName ASC";
        List<BillingCycleModel> billingCycleModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(BillingCycleModel.class));
        log.info("BillingCycleModel {} ", billingCycleModels);
        return billingCycleModels;
    }

    /**
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<BillingCycleModel> filter(String queryParam1, String queryValue1, String queryParam2, String queryValue2, String rowNumber) throws SQLException {
        String sql = "";
        if (queryParam1 == null && queryParam2 == null && queryValue1 == null && queryValue2 == null) {
            sql += "SELECT * FROM BillingCycle ORDER BY CreatedDate DESC";
        } else {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT * from BillingCycle WHERE ");

            if (queryValue1 != null && !queryValue1.isEmpty()
                    && !Objects.equals(queryValue1, "")
                    && !Objects.equals(queryValue1.toUpperCase(), "SELECT")
                    && !Objects.equals(queryValue1.toUpperCase(), "UNDEFINED")) {
                sqlBuilder.append("" + queryParam1 + " = '").append("" + queryValue1 + "").append("' AND ");
            }

            if (queryValue2 != null && !queryValue2.isEmpty() &&
                    !Objects.equals(queryValue2, "")
                    && !Objects.equals(queryValue2.toUpperCase(), "SELECT")
                    && !Objects.equals(queryValue1.toUpperCase(), "UNDEFINED")) {
                sqlBuilder.append("" + queryParam2 + " = '").append("" + queryValue2 + "").append("' AND ");
            }

            sqlBuilder.append("CreatedDate IS NOT NULL ");
            sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
            sql += sqlBuilder.toString();

        }
        log.info("sql {} ", sql);

        List<BillingCycleModel> billingCycleModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(BillingCycleModel.class));
        log.info("BillingCycleModel 222 {} ", billingCycleModels);
        return billingCycleModels;
    }
}
