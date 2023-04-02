package com.molcom.nms.GenericDatabaseUpdates.service;

import com.molcom.nms.general.dto.GenericResponse;

public interface IGenericTableUpdateService {
    GenericResponse<String> updateTableColumn(String tableName, String columnName, String columnValue, String columnConstraintKey, String columnConstraintValue) throws Exception;
}
