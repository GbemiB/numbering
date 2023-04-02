package com.molcom.nms.companyrep.repository;

import com.molcom.nms.companyrep.dto.CompRepModel;

import java.sql.SQLException;
import java.util.List;

public interface ICompRepRepository {
    int save(CompRepModel model) throws SQLException;

    int saveSelectedCompRep(CompRepModel model) throws SQLException;

    int edit(CompRepModel model, int compRepId) throws SQLException;

    int deleteById(int compRepId) throws SQLException;

    int deleteSelectedCompRep(int selectedCompReId) throws SQLException;

    CompRepModel findByCompRepId(int compRepId) throws SQLException;

    List<CompRepModel> getAll() throws SQLException;

    List<CompRepModel> findByUserId(String userId) throws SQLException;

    List<CompRepModel> findSelectedCompRep(String applicationId) throws SQLException;

    List<CompRepModel> findByUserIdWithImage(String userId) throws SQLException;

    List<CompRepModel> findSelectedCompRepWithoutImage(String applicationId) throws SQLException;

    List<CompRepModel> filterForRegularUser(String queryParam1, String queryValue1, String queryParam2,
                                            String queryValue2, String userId, String rowNumber) throws SQLException;

    List<CompRepModel> filterForAdminUser(String queryParam1, String queryValue1,
                                          String queryParam2, String queryValue2,
                                          String queryParam3, String queryValue3,
                                          String rowNumber) throws SQLException;
}
