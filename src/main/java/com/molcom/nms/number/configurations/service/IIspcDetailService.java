package com.molcom.nms.number.configurations.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.number.configurations.dto.ISPCDetailModel;

import java.util.List;

public interface IIspcDetailService {
    GenericResponse<ISPCDetailModel> saveIspcDetails(ISPCDetailModel ispcDetailModel) throws Exception;

    GenericResponse<ISPCDetailModel> deleteIspcDetails(String ispcDetailId) throws Exception;

    GenericResponse<List<ISPCDetailModel>> getIspcDetails(String applicationId) throws Exception;
}
