package com.molcom.nms.GenericDatabaseUpdates.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Slf4j
@Repository
public class GenericTableUpdateRepository implements IGenericTableUpdateRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * @param tableName
     * @param columnName
     * @param columnValue
     * @param columnConstraintKey
     * @param columnConstraintValue
     * @return
     * @throws SQLException
     */
    @Override
    public int updateTableColumn(String tableName, String columnName, String columnValue, String columnConstraintKey, String columnConstraintValue) throws SQLException {
        String sql = "UPDATE " + tableName + " SET " + columnName + "= '" + columnValue + "' WHERE " + columnConstraintKey + "= '" + columnConstraintValue + "'";
        log.info(sql);
        return jdbcTemplate.update(sql);
    }

    /**
     * @param tableName
     * @param columnName
     * @param columnValue
     * @param columnConstraintKey1
     * @param columnConstraintValue1
     * @param columnConstraintKey2
     * @param columnConstraintValue2
     * @return
     * @throws SQLException
     */
    @Override
    public int updateTableColumnWithTwoConditions(String tableName, String columnName, String columnValue, String columnConstraintKey1, String columnConstraintValue1, String columnConstraintKey2, String columnConstraintValue2) throws SQLException {
        String sql = "UPDATE " + tableName + " SET " + columnName + "= '" + columnValue + "' WHERE " + columnConstraintKey1 + "= '" + columnConstraintValue1 + "' AND " + columnConstraintKey2 + "= '" + columnConstraintValue2 + "'";
        log.info(sql);
        return jdbcTemplate.update(sql);
    }
}
