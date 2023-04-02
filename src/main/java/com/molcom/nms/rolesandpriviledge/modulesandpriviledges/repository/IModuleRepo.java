package com.molcom.nms.rolesandpriviledge.modulesandpriviledges.repository;

import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto.BulkUploadRequestModule;
import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto.BulkUploadResponseModel;
import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto.ModulesModel;

import java.sql.SQLException;
import java.util.List;

public interface IModuleRepo {

    int save(ModulesModel model, String roleType) throws SQLException;

    int deleteById(int modulesId) throws SQLException;

    int deleteByRoleType(String roleType) throws SQLException;

    List<ModulesModel> getAll() throws SQLException;

    List<ModulesModel> getModuleByRoleType(String roleType) throws SQLException;

    BulkUploadResponseModel bulkInsert(BulkUploadRequestModule bulkUploadRequest, String roleType) throws SQLException;
}
