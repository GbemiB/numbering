package com.molcom.nms.number.application.repository;

import com.molcom.nms.number.application.dto.SpecialNumberModel;

import java.sql.SQLException;
import java.util.List;

public interface ISpecialNumberRepository {
    int saveSpecialNoAppFistStep(SpecialNumberModel model) throws SQLException;

    int saveSpecialNoAppSecondStep(SpecialNumberModel model) throws SQLException;

    int saveSpecialNoAppThirdStep(SpecialNumberModel model) throws SQLException;

    List<SpecialNumberModel> getSpecialNoFistStep(String applicationId) throws SQLException;

    List<SpecialNumberModel> getSpecialNoSecondStep(String applicationId) throws SQLException;

    List<SpecialNumberModel> getSpecialNoFourthStep(String applicationId) throws SQLException;

    int saveDocFourthStep(String currentStep, String applicationId) throws SQLException;

    int deleteApplication(String applicationId) throws SQLException;


    List<SpecialNumberModel> filterSpecialNo(String CompanyName,
                                             String ApplicationId,
                                             String SubType,
                                             String ApplicationType,
                                             String AccessCode,
                                             String ApplicationStatus,
                                             String ApplicationPaymentStatus,
                                             String AllocationPaymentStatus,
                                             String StartDate,
                                             String EndDate,
                                             String rowNumber) throws SQLException;

    SpecialNumberModel getSpecialNoByApplicationId(String applicationId) throws SQLException;

    List<SpecialNumberModel> filterAdminSpecialNo(String CompanyName,
                                                  String ApplicationId,
                                                  String SubType,
                                                  String ApplicationType,
                                                  String AccessCode,
                                                  String ApplicationStatus,
                                                  String ApplicationPaymentStatus,
                                                  String AllocationPaymentStatus,
                                                  String StartDate,
                                                  String EndDate,
                                                  String rowNumber) throws SQLException;

    List<SpecialNumberModel> getAll() throws SQLException;

}
