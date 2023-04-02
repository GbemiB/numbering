package com.molcom.nms.fee.schedule.service;

import com.molcom.nms.fee.schedule.dto.FeeScheduleModel;
import com.molcom.nms.general.dto.GenericResponse;

import java.util.List;

public interface IFeeScheduleService {

    GenericResponse<FeeScheduleModel> save(FeeScheduleModel model) throws Exception;

    GenericResponse<FeeScheduleModel> edit(FeeScheduleModel model, int feeScheduleId) throws Exception;

    GenericResponse<FeeScheduleModel> deleteById(int feeScheduleId) throws Exception;

    GenericResponse<FeeScheduleModel> findById(int feeScheduleId) throws Exception;

    GenericResponse<List<FeeScheduleModel>> getAll() throws Exception;

    GenericResponse<List<FeeScheduleModel>> getShortCodeFee() throws Exception;

    GenericResponse<List<FeeScheduleModel>> filter(String queryParam1, String queryValue1,
                                                   String queryParam2, String queryValue2,
                                                   String queryParam3, String queryValue3,
                                                   String rowNumber) throws Exception;

}
