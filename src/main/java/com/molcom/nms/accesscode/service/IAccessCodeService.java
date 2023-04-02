package com.molcom.nms.accesscode.service;

import com.molcom.nms.accesscode.dto.AccessCodeBlkReq;
import com.molcom.nms.accesscode.dto.AccessCodeBlkRes;
import com.molcom.nms.accesscode.dto.AccessCodeModel;
import com.molcom.nms.general.dto.GenericResponse;

import java.sql.SQLException;
import java.util.List;

public interface IAccessCodeService {
    GenericResponse<AccessCodeModel> save(AccessCodeModel model) throws Exception;

    GenericResponse<AccessCodeModel> edit(AccessCodeModel model, int accessCodeId) throws Exception;

    GenericResponse<AccessCodeModel> deleteById(int accessCodeId) throws Exception;

    GenericResponse<List<AccessCodeModel>> getAll() throws Exception;

    GenericResponse<AccessCodeModel> findById(int accessCodeId) throws Exception;

    GenericResponse<List<AccessCodeModel>> getByCoverageName(String coverageArea) throws Exception;

    GenericResponse<List<AccessCodeModel>> getByAreaCode(String areaCode) throws Exception;

    GenericResponse<List<AccessCodeModel>> getByNumberSubType(String numberSubType) throws Exception;

    GenericResponse<AccessCodeBlkRes> bulkInsert(AccessCodeBlkReq bulkUploadRequest) throws SQLException;

    GenericResponse<List<AccessCodeModel>> filter(String queryParam1, String queryValue1, String queryParam2,
                                                  String queryValue2, String queryParam3, String queryValue3,
                                                  String queryParam4, String queryValue4, String rowNumber)
            throws Exception;

}
