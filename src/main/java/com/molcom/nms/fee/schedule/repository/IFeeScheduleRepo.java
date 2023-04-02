package com.molcom.nms.fee.schedule.repository;

import com.molcom.nms.fee.schedule.dto.FeeScheduleModel;

import java.sql.SQLException;
import java.util.List;

public interface IFeeScheduleRepo {

    int isExist(FeeScheduleModel model) throws SQLException;

    int save(FeeScheduleModel model) throws SQLException;

    int edit(FeeScheduleModel model, int feeScheduleId) throws SQLException;

    int deleteById(int feeScheduleId) throws SQLException;

    FeeScheduleModel findById(int feeScheduleId) throws SQLException;

    List<FeeScheduleModel> getShortCodeFee() throws SQLException;

    List<FeeScheduleModel> getShortCodeFeeByFeeName(String feeName) throws SQLException;

    List<FeeScheduleModel> filter(String queryParam1, String queryValue1,
                                  String queryParam2, String queryValue2,
                                  String queryParam3, String queryValue3,
                                  String rowNumber) throws SQLException;

    List<FeeScheduleModel> getAll() throws SQLException;

    List<FeeScheduleModel> getSpecificFeeSchedule(String feeType, String numberType, String numberSubType) throws SQLException;

}
