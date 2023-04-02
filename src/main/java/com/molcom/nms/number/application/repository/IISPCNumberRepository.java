package com.molcom.nms.number.application.repository;

import com.molcom.nms.number.application.dto.ISPCNumberModel;

import java.sql.SQLException;
import java.util.List;

public interface IISPCNumberRepository {

    int saveISPCFistStep(ISPCNumberModel model) throws SQLException;

    int saveISPCSecondStep(ISPCNumberModel model) throws SQLException;

    int saveISPCThirdStep(ISPCNumberModel model) throws SQLException;

    int saveISPCDocStep(String currentStep, String applicationId) throws SQLException;

    List<ISPCNumberModel> getIspcNoFistStep(String applicationId) throws SQLException;

    List<ISPCNumberModel> getIspcNoSecondStep(String applicationId) throws SQLException;

    List<ISPCNumberModel> getIspcNoFourthStep(String applicationId) throws SQLException;

    int deleteApplication(String applicationId) throws SQLException;

    List<ISPCNumberModel> filterISPC(String CompanyName,
                                     String ApplicationId,
                                     String ApplicationType,
                                     String ApplicationStatus,
                                     String StartDate,
                                     String EndDate,
                                     String rowNumber) throws SQLException;

    List<ISPCNumberModel> filterAdminISPC(String CompanyName,
                                          String ApplicationId,
                                          String ApplicationType,
                                          String ApplicationStatus,
                                          String StartDate,
                                          String EndDate,
                                          String rowNumber) throws SQLException;

    ISPCNumberModel getISPCByApplicationId(String applicationId) throws SQLException;

    List<ISPCNumberModel> getAll() throws SQLException;
}
