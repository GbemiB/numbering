package com.molcom.nms.fee.selectedFeeSchedule.repository;

import com.molcom.nms.fee.selectedFeeSchedule.dto.SelectedFeeScheduleModel;

import java.sql.SQLException;
import java.util.List;

public interface ISelectedFeeScheduleRepository {
    int save(SelectedFeeScheduleModel model) throws SQLException;

    List<SelectedFeeScheduleModel> findByApplicationId(String applicationId) throws SQLException;

    List<SelectedFeeScheduleModel> findByApplicationIdNewFee(String applicationId) throws SQLException;

    List<SelectedFeeScheduleModel> findByApplicationIdRenewalFee(String applicationId) throws SQLException;

    List<SelectedFeeScheduleModel> findByApplicationIdApplictaionFee(String applicationId) throws SQLException;
}
