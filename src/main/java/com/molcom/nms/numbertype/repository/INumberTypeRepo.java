package com.molcom.nms.numbertype.repository;

import com.molcom.nms.numbertype.dto.NumberTypeModel;

import java.sql.SQLException;
import java.util.List;

public interface INumberTypeRepo {

    int update(NumberTypeModel model, int numberTypeId) throws SQLException;

    NumberTypeModel findById(int numberTypeId) throws SQLException;

    List<NumberTypeModel> getAll() throws SQLException;

    List<NumberTypeModel> getByNumberSubType(String numberSubType) throws SQLException;

    List<NumberTypeModel> getForShortCodeType(String shortCodeType) throws SQLException;

    List<NumberTypeModel> filter(String queryParam, String queryValue, String rowNumber) throws SQLException;

}
