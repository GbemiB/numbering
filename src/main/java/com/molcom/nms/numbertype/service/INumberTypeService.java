package com.molcom.nms.numbertype.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.numbertype.dto.NumberTypeModel;

import java.util.List;

public interface INumberTypeService {

    GenericResponse<NumberTypeModel> update(NumberTypeModel model, int numberTypeId) throws Exception;

    GenericResponse<List<NumberTypeModel>> getAll() throws Exception;

    GenericResponse<NumberTypeModel> findById(int numberTyppeId) throws Exception;

    GenericResponse<List<NumberTypeModel>> filter(String queryParam, String queryValue, String rowNumber) throws Exception;
}


