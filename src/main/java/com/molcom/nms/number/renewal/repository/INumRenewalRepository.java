package com.molcom.nms.number.renewal.repository;

import com.molcom.nms.number.renewal.dto.NumRenewalModel;

import java.sql.SQLException;
import java.util.List;

public interface INumRenewalRepository {
    int updateAfterRenewal(String applicationId, String companyAllocatedTo) throws SQLException;

    int save(NumRenewalModel numRenewalModel);

    int countIfNoExist(String appId) throws SQLException;

    NumRenewalModel getByRenewalId(int renewalId) throws SQLException;

    NumRenewalModel getByApplicationId(String applicationId) throws SQLException;

    List<NumRenewalModel> getAll() throws SQLException;

    List<NumRenewalModel> filterForRegularUser(String queryParam1, String queryValue1,
                                               String queryParam2, String queryValue2,
                                               String queryParam3, String queryValue3,
                                               String queryParam4, String queryValue4,
                                               String queryParam5, String queryValue5,
                                               String companyName,
                                               String rowNumber) throws SQLException;

    List<NumRenewalModel> filterForAdminUser(String queryParam1, String queryValue1,
                                             String queryParam2, String queryValue2,
                                             String queryParam3, String queryValue3,
                                             String queryParam4, String queryValue4,
                                             String queryParam5, String queryValue5,
                                             String queryParam6, String queryValue6,
                                             String queryParam7, String queryValue7,
                                             String rowNumber) throws SQLException;
}
