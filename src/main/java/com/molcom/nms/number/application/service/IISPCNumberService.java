package com.molcom.nms.number.application.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.number.application.dto.ISPCNumberModel;
import com.molcom.nms.number.application.dto.ISPCNumberResponse;

import java.sql.SQLException;
import java.util.List;

public interface IISPCNumberService {
    GenericResponse<ISPCNumberModel> saveISPCNumberFistStep(ISPCNumberModel model) throws Exception;

    GenericResponse<ISPCNumberModel> saveISPCNumberSecondStep(ISPCNumberModel model) throws Exception;

    GenericResponse<ISPCNumberModel> saveISPCNumberThirdStep(ISPCNumberModel model) throws Exception;

    GenericResponse<ISPCNumberModel> saveISPCNumberDocStep(String currentStep, String applicationId) throws Exception;

    GenericResponse<ISPCNumberModel> deleteApplication(String applicationId) throws SQLException;

    GenericResponse<List<ISPCNumberModel>> getFistStep(String applicationId) throws SQLException;

    GenericResponse<List<ISPCNumberModel>> getSecondStep(String applicationId) throws SQLException;

    GenericResponse<List<ISPCNumberModel>> getFourthStep(String applicationId) throws SQLException;

    GenericResponse<List<ISPCNumberModel>> filterISPCNumber(String companyName,
                                                            String ApplicationId,
                                                            String ApplicationType,
                                                            String ApplicationStatus,
                                                            String StartDate,
                                                            String EndDate,
                                                            String rowNumber) throws Exception;

    GenericResponse<List<ISPCNumberModel>> filterAdminISPCNumber(String CompanyName,
                                                                 String ApplicationId,
                                                                 String ApplicationType,
                                                                 String ApplicationStatus,
                                                                 String StartDate,
                                                                 String EndDate,
                                                                 String rowNumber) throws Exception;


    ISPCNumberResponse getISPCNumberById(String applicationId, String userId) throws Exception;

    GenericResponse<List<ISPCNumberModel>> getAll() throws Exception;
}
