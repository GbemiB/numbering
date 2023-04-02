package com.molcom.nms.companyrep.service;

import com.molcom.nms.companyrep.dto.CompRepModel;
import com.molcom.nms.general.dto.GenericResponse;

import java.util.List;

public interface ICompRepService {
    GenericResponse<CompRepModel> save(CompRepModel model) throws Exception;

    GenericResponse<CompRepModel> saveSelectedRep(CompRepModel model) throws Exception;

    GenericResponse<CompRepModel> edit(CompRepModel model, int compRepId) throws Exception;

    GenericResponse<CompRepModel> deleteById(int compRepId) throws Exception;

    GenericResponse<CompRepModel> deleteSelectedRep(int selectedCompReId) throws Exception;

    GenericResponse<CompRepModel> findByCompRepId(int compRepId) throws Exception;

    GenericResponse<List<CompRepModel>> findByUserId(String userId) throws Exception;

    GenericResponse<List<CompRepModel>> getAll() throws Exception;

    GenericResponse<List<CompRepModel>> findSelectedRep(String userId) throws Exception;

    GenericResponse<List<CompRepModel>> findByUserIdWithoutImage(String userId) throws Exception;

    GenericResponse<List<CompRepModel>> findSelectedRepWithoutImage(String userId) throws Exception;

    GenericResponse<List<CompRepModel>> filterForRegularUser(String queryParam1, String queryValue1,
                                                             String queryParam2, String queryValue2,
                                                             String userId, String rowNumber) throws Exception;

    GenericResponse<List<CompRepModel>> filterForAdminUser(String queryParam1, String queryValue1,
                                                           String queryParam2, String queryValue2,
                                                           String queryParam3, String queryValue3,
                                                           String rowNumber) throws Exception;
}
