package com.molcom.nms.areacode.service;

import com.molcom.nms.areacode.dto.AreaCodeBlkReq;
import com.molcom.nms.areacode.dto.AreaCodeBlkRes;
import com.molcom.nms.areacode.dto.AreaCodeModel;
import com.molcom.nms.general.dto.GenericResponse;

import java.util.List;

public interface IAreaCodeService {
    GenericResponse<AreaCodeModel> save(AreaCodeModel model) throws Exception;

    GenericResponse<AreaCodeBlkRes> bulkUpload(AreaCodeBlkReq bulkUploadRequest) throws Exception;

    GenericResponse<AreaCodeModel> edit(AreaCodeModel model, int areaId) throws Exception;

    GenericResponse<AreaCodeModel> deleteById(int areaId) throws Exception;

    GenericResponse<List<AreaCodeModel>> getAll() throws Exception;

    GenericResponse<List<AreaCodeModel>> getAreaCodeByCoverageArea(String coverageArea) throws Exception;

    GenericResponse<AreaCodeModel> findById(int areaId) throws Exception;

    GenericResponse<List<AreaCodeModel>> filter(String queryParam1, String queryValue1, String queryParam2, String queryValue2, String rowNumber) throws Exception;

}
