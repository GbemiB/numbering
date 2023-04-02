package com.molcom.nms.number.renewal.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.number.renewal.dto.NumRenewalModel;
import com.molcom.nms.number.renewal.dto.NumRenewalObject;

import java.util.List;

public interface INumRenewalService {
    GenericResponse<NumRenewalObject> getByRenewalId(int renewalId) throws Exception;

    GenericResponse<List<NumRenewalModel>> filterForRegularUser(String queryParam1, String queryValue1,
                                                                String queryParam2, String queryValue2,
                                                                String queryParam3, String queryValue3,
                                                                String queryParam4, String queryValue4,
                                                                String queryParam5, String queryValue5,
                                                                String companyName,
                                                                String rowNumber) throws Exception;

    GenericResponse<List<NumRenewalModel>> filterForAdminUser(String queryParam1, String queryValue1,
                                                              String queryParam2, String queryValue2,
                                                              String queryParam3, String queryValue3,
                                                              String queryParam4, String queryValue4,
                                                              String queryParam5, String queryValue5,
                                                              String queryParam6, String queryValue6,
                                                              String queryParam7, String queryValue7,
                                                              String rowNumber) throws Exception;

    GenericResponse<List<NumRenewalModel>> getAll() throws Exception;
}
