package com.molcom.nms.rolesandpriviledge.userroles.repository;

import com.molcom.nms.rolesandpriviledge.userroles.dto.AssignRoleModel;
import com.molcom.nms.rolesandpriviledge.userroles.dto.UserRoleModel;

import java.sql.SQLException;
import java.util.List;

public interface IUserRoleRepo {

    int doesRoleExist(String roleType) throws Exception;

    int save(UserRoleModel model) throws SQLException;

    int edit(UserRoleModel model) throws SQLException;

    int assignRoleToUser(AssignRoleModel model) throws Exception;

    List<UserRoleModel> getAllRoles(String rowNumber) throws Exception;

    List<AssignRoleModel> getRolesAssignedToUser(String username) throws Exception;

    int deleteById(int userRoleId) throws SQLException;

    UserRoleModel findById(int userRoleId) throws SQLException;

    UserRoleModel findByRoleName(String userRoleName) throws SQLException;

    List<UserRoleModel> filter(String queryParam, String queryValue, String rowNumber) throws SQLException;
}

