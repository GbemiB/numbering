package com.molcom.nms.categoryAndService.repository;

import com.molcom.nms.categoryAndService.dto.ShortCodeServiceModel;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class ShortCodeServiceRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @param serviceName
     * @return
     * @throws SQLException
     */
    public int isExist(String serviceName) throws SQLException {
        String sql = "SELECT count(*) FROM ShortCodeService WHERE ServiceName=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, serviceName);
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    public int save(ShortCodeServiceModel model) throws SQLException {
        String sql = "INSERT INTO ShortCodeService (ShortCodeCategory, ServiceName, ServiceCode, ServiceDescription,CreatedBy, CreatedDate ) " +
                "VALUES(?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                model.getShortCodeCategory(),
                model.getServiceName(),
                model.getServiceCode(),
                model.getServiceDescription(),
                model.getCreatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * @return
     * @throws SQLException
     */
    public List<ShortCodeServiceModel> getAll() throws SQLException {
        String sql = "SELECT * FROM ShortCodeService";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(ShortCodeServiceModel.class));
    }

    /**
     * @param shortCodeCategory
     * @return
     * @throws SQLException
     */
    public List<ShortCodeServiceModel> getByCategoryName(String shortCodeCategory) throws SQLException {
        String sql = "SELECT * FROM ShortCodeService WHERE ShortCodeCategory=?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(ShortCodeServiceModel.class), shortCodeCategory);
    }

    /**
     * @param serviceName
     * @param shortCodeCategory
     * @return
     * @throws SQLException
     */
    public int deleteById(String serviceName, String shortCodeCategory) throws SQLException {
        String sql = "DELETE FROM ShortCodeService WHERE ServiceName=? AND ShortCodeCategory=?";
        return jdbcTemplate.update(sql, serviceName, shortCodeCategory);
    }

}
