package com.molcom.nms.number.application.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.number.application.dto.SpecialNumberModel;
import com.molcom.nms.number.application.dto.SpecialNumberResponse;

import java.sql.SQLException;
import java.util.List;

public interface ISpecialNumberService {
    GenericResponse<SpecialNumberModel> saveSpecialNoFistStep(SpecialNumberModel model) throws Exception;

    GenericResponse<SpecialNumberModel> saveSpecialNoSecondStep(SpecialNumberModel model) throws Exception;

    GenericResponse<SpecialNumberModel> saveSpecialNoThirdStep(SpecialNumberModel model) throws Exception;

    GenericResponse<SpecialNumberModel> saveSpecialDocStep(String currentStep, String applicationId) throws Exception;

    GenericResponse<SpecialNumberModel> deleteApplication(String applicationId) throws SQLException;

    GenericResponse<List<SpecialNumberModel>> getFistStep(String applicationId) throws SQLException;

    GenericResponse<List<SpecialNumberModel>> getSecondStep(String applicationId) throws SQLException;

    GenericResponse<List<SpecialNumberModel>> getFourthStep(String applicationId) throws SQLException;

    GenericResponse<List<SpecialNumberModel>> filterSpecialNo(String companyName,
                                                              String ApplicationId,
                                                              String SubType,
                                                              String ApplicationType,
                                                              String AccessCode,
                                                              String ApplicationStatus,
                                                              String ApplicationPaymentStatus,
                                                              String AllocationPaymentStatus,
                                                              String StartDate,
                                                              String EndDate,
                                                              String rowNumber) throws Exception;

    GenericResponse<List<SpecialNumberModel>> filterAdminSpecialNo(String CompanyName,
                                                                   String ApplicationId,
                                                                   String SubType,
                                                                   String ApplicationType,
                                                                   String AccessCode,
                                                                   String ApplicationStatus,
                                                                   String ApplicationPaymentStatus,
                                                                   String AllocationPaymentStatus,
                                                                   String StartDate,
                                                                   String EndDate,
                                                                   String rowNumber) throws Exception;

    SpecialNumberResponse getSpecialNoById(String applicationId, String userId) throws Exception;

    GenericResponse<List<SpecialNumberModel>> getAll() throws Exception;
}
