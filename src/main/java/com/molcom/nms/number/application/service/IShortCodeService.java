package com.molcom.nms.number.application.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.number.application.dto.ShortCodeModel;
import com.molcom.nms.number.application.dto.ShortCodeResponse;

import java.sql.SQLException;
import java.util.List;

public interface IShortCodeService {
    GenericResponse<ShortCodeModel> saveShortCodeAppFistStep(ShortCodeModel model) throws Exception;

    GenericResponse<ShortCodeModel> saveShortCodeAppSecondStep(ShortCodeModel model) throws Exception;

    GenericResponse<ShortCodeModel> saveShortCodeAppThirdStep(ShortCodeModel model) throws Exception;

    GenericResponse<ShortCodeModel> saveShortCodeDocStep(String currentStep, String applicationId) throws Exception;

    GenericResponse<ShortCodeModel> deleteApplication(String applicationId) throws SQLException;

    GenericResponse<List<ShortCodeModel>> getFistStep(String applicationId) throws SQLException;

    GenericResponse<List<ShortCodeModel>> getSecondStep(String applicationId) throws SQLException;

    GenericResponse<List<ShortCodeModel>> getFourthStep(String applicationId) throws SQLException;

    GenericResponse<List<ShortCodeModel>> filterShortCodes(
            String companyName,
            String ApplicationId,
            String ShortCodeCategory,
            String ShortCodeService,
            String ApplicationType,
            String ApplicationStatus,
            String ApplicationPaymentStatus,
            String AllocationPaymentStatus,
            String StartDate,
            String EndDate,
            String rowNumber

    ) throws Exception;

    GenericResponse<List<ShortCodeModel>> filterAdminShortCodes(
            String CompanyName,
            String ApplicationId,
            String ShortCodeCategory,
            String ShortCodeService,
            String ApplicationType,
            String ApplicationStatus,
            String ApplicationPaymentStatus,
            String AllocationPaymentStatus,
            String StartDate,
            String EndDate,
            String rowNumber

    ) throws Exception;

    ShortCodeResponse getShortCodeById(String applicationId, String userId) throws Exception;

    GenericResponse<List<ShortCodeModel>> getAll() throws Exception;
}

