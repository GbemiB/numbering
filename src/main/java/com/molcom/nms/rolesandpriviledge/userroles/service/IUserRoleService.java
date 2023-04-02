package com.molcom.nms.rolesandpriviledge.userroles.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.rolesandpriviledge.userroles.dto.UserObjectModel;
import com.molcom.nms.rolesandpriviledge.userroles.dto.UserRoleModel;

import java.util.List;

public interface IUserRoleService {
    GenericResponse<UserObjectModel> save(UserObjectModel model) throws Exception;

    GenericResponse<UserObjectModel> replace(UserObjectModel model) throws Exception;

    GenericResponse<UserRoleModel> deleteById(int userRoleId, String userRoleType) throws Exception;

    GenericResponse<UserObjectModel> findById(int userRoleId, String userRoleType) throws Exception;

    GenericResponse<UserObjectModel> findByRoleName(String userRoleType) throws Exception;

    GenericResponse<List<UserRoleModel>> getAllRows(String rowNumber) throws Exception;

    GenericResponse<List<UserRoleModel>> filter(String queryParam, String queryValue, String rowNumber) throws Exception;
}
