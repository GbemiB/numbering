package com.molcom.nms.number.application.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.number.application.dto.StandardNumberModel;
import com.molcom.nms.number.application.dto.StandardNumberResponse;

import java.sql.SQLException;
import java.util.List;

public interface IStandardNumberService {
    GenericResponse<StandardNumberModel> saveStandardNoFistStep(StandardNumberModel model) throws Exception;

    GenericResponse<StandardNumberModel> saveStandardNoSecondStep(StandardNumberModel model) throws Exception;

    GenericResponse<StandardNumberModel> saveStandardNoThirdStep(StandardNumberModel model) throws Exception;

    GenericResponse<StandardNumberModel> saveStandardDocStep(String currentStep, String applicationId) throws Exception;

    GenericResponse<StandardNumberModel> deleteApplication(String applicationId) throws SQLException;

    GenericResponse<List<StandardNumberModel>> getFistStep(String applicationId) throws SQLException;

    GenericResponse<List<StandardNumberModel>> getSecondStep(String applicationId) throws SQLException;

    GenericResponse<List<StandardNumberModel>> getFourthStep(String applicationId) throws SQLException;

    GenericResponse<List<StandardNumberModel>> filterStandardNo(String companyName,
                                                                String ApplicationId,
                                                                String SubType,
                                                                String ApplicationType,
                                                                String CoverageArea,
                                                                String AreaCode,
                                                                String AccessCode,
                                                                String ApplicationStatus,
                                                                String ApplicationPaymentStatus,
                                                                String AllocationPaymentStatus,
                                                                String StartDate,
                                                                String EndDate,
                                                                String rowNumber) throws Exception;

    GenericResponse<List<StandardNumberModel>> filterAdminStandardNo(String ApplicationId,
                                                                     String CompanyName,
                                                                     String SubType,
                                                                     String ApplicationType,
                                                                     String CoverageArea,
                                                                     String AreaCode,
                                                                     String AccessCode,
                                                                     String ApplicationStatus,
                                                                     String ApplicationPaymentStatus,
                                                                     String AllocationPaymentStatus,
                                                                     String StartDate,
                                                                     String EndDate,
                                                                     String rowNumber) throws Exception;

    StandardNumberResponse getStandardNoById(String applicationId, String userId) throws Exception;

    GenericResponse<List<StandardNumberModel>> getAll() throws Exception;
}

