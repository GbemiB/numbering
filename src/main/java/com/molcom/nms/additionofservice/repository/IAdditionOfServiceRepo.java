package com.molcom.nms.additionofservice.repository;

import com.molcom.nms.additionofservice.dto.AdditionOfServiceModel;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;

import java.sql.SQLException;
import java.util.List;

public interface IAdditionOfServiceRepo {
    int doesExist(AdditionOfServiceModel model) throws SQLException;

    int save(AdditionOfServiceModel model) throws SQLException;

    List<SelectedNumberModel> getAllocatedNos(String companyName) throws SQLException;

    int deleteById(int additionOfServiceId) throws SQLException;

    AdditionOfServiceModel findById(String applicationId) throws SQLException;

    List<AdditionOfServiceModel> getAll() throws SQLException;

    List<AdditionOfServiceModel> getAllByOrganisation(String organisation) throws SQLException;

    List<AdditionOfServiceModel> filter(String queryParam1, String queryValue1, String queryParam2,
                                        String queryValue2,
                                        String queryValue3,
                                        String queryValue4, String rowNumber) throws SQLException;

    List<AdditionOfServiceModel> filterForRegularUser(String companyName, String queryParam1, String queryValue1, String queryParam2,
                                                      String queryValue2,
                                                      String queryValue3,
                                                      String queryValue4, String rowNumber) throws SQLException;


}
