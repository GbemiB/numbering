package com.molcom.nms.categoryAndService.repository;

import com.molcom.nms.categoryAndService.dto.ShortCodeCategoryModel;
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
public class ShortCodeCategoryRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @param categoryName
     * @return
     * @throws SQLException
     */
    public int isExist(String categoryName) throws SQLException {
        String sql = "SELECT count(*) FROM ShortCodeCategory WHERE CategoryName=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, categoryName);
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    public int save(ShortCodeCategoryModel model) throws SQLException {
        String sql = "INSERT INTO ShortCodeCategory (CategoryName, CategoryCode, CategoryFrequency, CategoryNumberLength,CreatedBy, CreatedDate ) " +
                "VALUES(?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                model.getCategoryName(),
                model.getCategoryCode(),
                model.getCategoryFrequency(),
                model.getCategoryNumberLength(),
                model.getCreatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * @return
     * @throws SQLException
     */
    public List<ShortCodeCategoryModel> getAll() throws SQLException {
        String sql = "SELECT * FROM ShortCodeCategory";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(ShortCodeCategoryModel.class));
    }

    /**
     * @param categoryName
     * @return
     * @throws SQLException
     */
    public int deleteById(String categoryName) throws SQLException {
        String sql = "DELETE FROM ShortCodeCategory WHERE CategoryName=?";
        return jdbcTemplate.update(sql, categoryName);
    }
}
