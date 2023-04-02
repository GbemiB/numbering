package com.molcom.nms.additionofservice.service;

import com.molcom.nms.additionofservice.dto.AdditionOfServiceModel;
import com.molcom.nms.additionofservice.dto.AvailableForAdditionOfService;
import com.molcom.nms.general.dto.GenericResponse;

import java.sql.SQLException;
import java.util.List;

public interface IAdditionOfService {
    GenericResponse<AdditionOfServiceModel> save(AdditionOfServiceModel model) throws Exception;

    GenericResponse<List<AvailableForAdditionOfService>> getAllocatedNos(String companyName) throws SQLException;

    GenericResponse<AdditionOfServiceModel> deleteById(int additionOfServiceId) throws Exception;

    GenericResponse<List<AdditionOfServiceModel>> getAll() throws Exception;

    GenericResponse<List<AdditionOfServiceModel>> getAllByOrganisation(String organisation) throws Exception;

    GenericResponse<AdditionOfServiceModel> findById(String additionOfServiceId) throws Exception;

    GenericResponse<List<AdditionOfServiceModel>> filter(String queryParam1, String queryValue1, String queryParam2,
                                                         String queryValue2,
                                                         String queryValue3,
                                                         String queryValue4, String rowNumber) throws Exception;

    GenericResponse<List<AdditionOfServiceModel>> filterForRegularUser(String companyName, String queryParam1, String queryValue1, String queryParam2,
                                                                       String queryValue2,
                                                                       String queryValue3,
                                                                       String queryValue4, String rowNumber) throws Exception;

}
