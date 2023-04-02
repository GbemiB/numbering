package com.molcom.nms.accesscode.repository;

import com.molcom.nms.accesscode.dto.AccessCodeBlkReq;
import com.molcom.nms.accesscode.dto.AccessCodeBlkRes;
import com.molcom.nms.accesscode.dto.AccessCodeModel;

import java.sql.SQLException;
import java.util.List;

public interface IAccessCodeRepo {
    int accessCodeCount(String accessCode) throws SQLException;

    int save(AccessCodeModel model) throws SQLException;

    int edit(AccessCodeModel model, int accessCodeId) throws SQLException;

    int deleteById(int accessCodeId) throws SQLException;

    AccessCodeModel findById(int accessCodeId) throws SQLException;

    List<AccessCodeModel> getAll() throws SQLException;

    List<AccessCodeModel> getByCoverageName(String coverageArea) throws SQLException;

    List<AccessCodeModel> getByAreaCode(String areaCode) throws SQLException;

    List<AccessCodeModel> getByNumberSubType(String numberSubType) throws SQLException;

    AccessCodeBlkRes bulkInsert(AccessCodeBlkReq bulkUploadRequest) throws SQLException;

    List<AccessCodeModel> filter(String queryParam1, String queryValue1, String queryParam2,
                                 String queryValue2, String queryParam3, String queryValue3,
                                 String queryParam4, String queryValue4, String rowNumber) throws SQLException;
}
