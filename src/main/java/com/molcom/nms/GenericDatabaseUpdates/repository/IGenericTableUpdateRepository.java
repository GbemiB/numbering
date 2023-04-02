package com.molcom.nms.GenericDatabaseUpdates.repository;

import java.sql.SQLException;

public interface IGenericTableUpdateRepository {
    int updateTableColumn(String tableName, String columnName, String columnValue, String columnConstraintKey, String columnConstraintValue) throws SQLException;

    int updateTableColumnWithTwoConditions(String tableName, String columnName, String columnValue, String columnConstraintKey1, String columnConstraintValue1, String columnConstraintKey2, String columnConstraintValue2) throws SQLException;
}
