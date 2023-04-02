package com.molcom.nms.number.application.repository;

import com.molcom.nms.number.application.dto.StandardNumberModel;

import java.sql.SQLException;
import java.util.List;


public interface IStandardNumberRepository {
    int saveStandardNoFistStep(StandardNumberModel model) throws SQLException;

    int saveStandardNoSecondStep(StandardNumberModel model) throws SQLException;

    int saveStandardNoThirdStep(StandardNumberModel model) throws SQLException;

    int saveDocFourthStep(String currentStep, String applicationId) throws SQLException;

    List<StandardNumberModel> getStandardNoFistStep(String applicationId) throws SQLException;

    List<StandardNumberModel> getStandardNoSecondStep(String applicationId) throws SQLException;

    List<StandardNumberModel> getStandardNoFourthStep(String applicationId) throws SQLException;

    int deleteApplication(String applicationId) throws SQLException;

    List<StandardNumberModel> filterStandardNo(String CompanyName,
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
                                               String rowNumber) throws SQLException;

    List<StandardNumberModel> filterAminStandardNo(String CompanyName,
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
                                                   String rowNumber) throws SQLException;

    StandardNumberModel getStandardNoByApplicationId(String applicationId) throws SQLException;

    List<StandardNumberModel> getAll() throws SQLException;
}
