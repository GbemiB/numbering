package com.molcom.nms.coveragearea.repository;

import com.molcom.nms.coveragearea.dto.BulkUploadRequest;
import com.molcom.nms.coveragearea.dto.BulkUploadResponse;
import com.molcom.nms.coveragearea.dto.CoverageAreaModel;

import java.sql.SQLException;
import java.util.List;

public interface ICoverageAreaRepo {
    int accessCodeCount(String coverageName) throws SQLException;

    int save(CoverageAreaModel model) throws SQLException;

    int edit(CoverageAreaModel model, int coverageId) throws SQLException;

    int deleteById(int coverageId) throws SQLException;

    CoverageAreaModel findById(int coverageId) throws SQLException;

    List<CoverageAreaModel> getAll() throws SQLException;

    List<CoverageAreaModel> getByCoverageType(String coverageType) throws SQLException;

    BulkUploadResponse bulkInsert(BulkUploadRequest bulkUploadRequest) throws SQLException;

    List<CoverageAreaModel> filter(String queryParam1, String queryValue1, String queryParam2, String queryValue2, String rowNumber) throws SQLException;
}
