package com.molcom.nms.fee.type.repository;

import com.molcom.nms.fee.type.dto.FeeTypeModel;

import java.sql.SQLException;
import java.util.List;

public interface IFeeTypeRepository {
    int feeTypeCount(String feeTypeName) throws SQLException;

    int save(FeeTypeModel model) throws SQLException;

    int deleteById(int feeTypeId) throws SQLException;

    FeeTypeModel findById(int feeTypeId) throws SQLException;

    List<FeeTypeModel> getAll() throws SQLException;

    List<FeeTypeModel> filter(String queryParam, String queryValue, String rowNumber) throws SQLException;
}
