package com.molcom.nms.fee.type.service;

import com.molcom.nms.fee.type.dto.FeeTypeModel;
import com.molcom.nms.general.dto.GenericResponse;

import java.util.List;

public interface IFeeTypeService {
    GenericResponse<FeeTypeModel> save(FeeTypeModel model) throws Exception;

    GenericResponse<FeeTypeModel> deleteById(int feeTypeId) throws Exception;

    GenericResponse<List<FeeTypeModel>> getAll() throws Exception;

    GenericResponse<FeeTypeModel> findById(int feeTypeId) throws Exception;

    GenericResponse<List<FeeTypeModel>> filter(String queryParam, String queryValue, String rowNumber) throws Exception;
}
