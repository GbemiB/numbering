package com.molcom.nms.number.application.repository;

import com.molcom.nms.number.application.dto.ShortCodeModel;

import java.sql.SQLException;
import java.util.List;

public interface IShortCodeRepository {
    int saveShortCodeAppFistStep(ShortCodeModel model) throws SQLException;

    int saveShortCodeAppSecondStep(ShortCodeModel model) throws SQLException;

    int saveShortCodeAppThirdStep(ShortCodeModel model) throws SQLException;

    int saveDocFourthStep(String currentStep, String applicationId) throws SQLException;

    List<ShortCodeModel> getShortNoFistStep(String applicationId) throws SQLException;

    List<ShortCodeModel> getShortNoSecondStep(String applicationId) throws SQLException;

    List<ShortCodeModel> getShortNoFourthStep(String applicationId) throws SQLException;

    int deleteApplication(String applicationId) throws SQLException;


    List<ShortCodeModel> filterShortCodes(String CompanyName,
                                          String ApplicationId,
                                          String ShortCodeCategory,
                                          String ShortCodeService,
                                          String ApplicationType,
                                          String ApplicationStatus,
                                          String ApplicationPaymentStatus,
                                          String AllocationPaymentStatus,
                                          String StartDate,
                                          String EndDate,
                                          String rowNumber) throws SQLException;

    List<ShortCodeModel> filterAdminShortCodes(String CompanyName,
                                               String ApplicationId,
                                               String ShortCodeCategory,
                                               String ShortCodeService,
                                               String ApplicationType,
                                               String ApplicationStatus,
                                               String ApplicationPaymentStatus,
                                               String AllocationPaymentStatus,
                                               String StartDate,
                                               String EndDate,
                                               String rowNumber) throws SQLException;

    ShortCodeModel getShortCodeByApplId(String applicationId) throws SQLException;

    List<ShortCodeModel> getAll() throws SQLException;

}
