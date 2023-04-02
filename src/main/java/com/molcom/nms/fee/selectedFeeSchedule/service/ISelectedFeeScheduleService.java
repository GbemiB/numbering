package com.molcom.nms.fee.selectedFeeSchedule.service;

import com.molcom.nms.fee.selectedFeeSchedule.dto.SelectedFeeScheduleModel;
import com.molcom.nms.general.dto.GenericResponse;

import java.sql.SQLException;
import java.util.List;

public interface ISelectedFeeScheduleService {
    GenericResponse<SelectedFeeScheduleModel> save(SelectedFeeScheduleModel model) throws SQLException;

    GenericResponse<List<SelectedFeeScheduleModel>> findByApplicationId(String applicationId) throws SQLException;
}
