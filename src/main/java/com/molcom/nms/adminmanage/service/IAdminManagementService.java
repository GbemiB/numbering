package com.molcom.nms.adminmanage.service;

import com.molcom.nms.adminmanage.dto.AdminManagementModel;
import com.molcom.nms.general.dto.GenericResponse;

import java.util.List;

public interface IAdminManagementService {

    GenericResponse<AdminManagementModel> save(AdminManagementModel model) throws Exception;

    GenericResponse<AdminManagementModel> editRole(AdminManagementModel model, int adminManageId) throws Exception;

    GenericResponse<AdminManagementModel> editSignature(String signature, int adminManageId) throws Exception;

    GenericResponse<List<AdminManagementModel>> getAll(String rowNumber) throws Exception;

    GenericResponse<List<AdminManagementModel>> filter(String queryParam1, String queryValue1,
                                                       String queryParam2, String queryValue2,
                                                       String queryParam3, String queryValue3,
                                                       String rowNumber) throws Exception;
}
