package com.molcom.nms.adminmanage.repository;

import com.molcom.nms.adminmanage.dto.AdminManagementModel;

import java.sql.SQLException;
import java.util.List;

public interface IAdminManagementRepo {

    int save(AdminManagementModel model) throws SQLException;

    boolean persistUser(AdminManagementModel model) throws SQLException;

    int editRole(AdminManagementModel model, int adminManageId) throws SQLException;

    int editSignature(String signature, int adminManageId) throws SQLException;

    List<AdminManagementModel> getAll(String rowNumber) throws Exception;

    List<AdminManagementModel> filter(String queryParam1, String queryValue1,
                                      String queryParam2, String queryValue2,
                                      String queryParam3, String queryValue3,
                                      String rowNumber) throws SQLException;

    List<AdminManagementModel> getAdminByUsername(String username);

    List<AdminManagementModel> getAllAdminWithRole(String roleName);

    List<AdminManagementModel> getRolesOfAdmin(String username);

    List<String> getEmailOfFellowAdmin(String username);

    List<String> getAdminEmails();

    List<String> getCollaboratorAdmins();

    List<String> getEmailOfCollaboratorAdminForOrgan(String organisation);

    List<String> getEmailOfAllAdminForOrganisation(String organisation);

    String getEmailOrganisation(String username);

}

