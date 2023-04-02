package com.molcom.nms.coveragearea.service;

import com.molcom.nms.coveragearea.dto.BulkUploadRequest;
import com.molcom.nms.coveragearea.dto.BulkUploadResponse;
import com.molcom.nms.coveragearea.dto.CoverageAreaModel;
import com.molcom.nms.general.dto.GenericResponse;

import java.util.List;

public interface ICoverageAreaService {
    GenericResponse<CoverageAreaModel> save(CoverageAreaModel model) throws Exception;

    GenericResponse<BulkUploadResponse> bulkUpload(BulkUploadRequest bulkUploadRequest) throws Exception;

    GenericResponse<CoverageAreaModel> edit(CoverageAreaModel model, int coverageId) throws Exception;

    GenericResponse<CoverageAreaModel> deleteById(int coverageId) throws Exception;

    GenericResponse<List<CoverageAreaModel>> getAll() throws Exception;


    GenericResponse<List<CoverageAreaModel>> getCoverageType(String coverageType) throws Exception;

    GenericResponse<CoverageAreaModel> findById(int coverageId) throws Exception;

    GenericResponse<List<CoverageAreaModel>> filter(String queryParam1, String queryValue1, String queryParam2,
                                                    String queryValue2, String rowNumber) throws Exception;
}
