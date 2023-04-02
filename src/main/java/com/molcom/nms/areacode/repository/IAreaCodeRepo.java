package com.molcom.nms.areacode.repository;

import com.molcom.nms.areacode.dto.AreaCodeBlkReq;
import com.molcom.nms.areacode.dto.AreaCodeBlkRes;
import com.molcom.nms.areacode.dto.AreaCodeModel;

import java.sql.SQLException;
import java.util.List;

public interface IAreaCodeRepo {
    int areaCodeCount(String areaCode) throws SQLException;

    int save(AreaCodeModel model) throws SQLException;

    int edit(AreaCodeModel model, int areaId) throws SQLException;

    int deleteById(int areaId) throws SQLException;

    AreaCodeModel findById(int areaId) throws SQLException;

    List<AreaCodeModel> getAll() throws SQLException;

    List<AreaCodeModel> getAreaCodeByCoverageArea(String coverageArea) throws SQLException;

    AreaCodeBlkRes bulkInsert(AreaCodeBlkReq bulkUploadRequest) throws SQLException;

    List<AreaCodeModel> filter(String queryParam1, String queryValue1, String queryParam2, String queryValue2, String rowNumber) throws SQLException;
}
