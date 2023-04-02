package com.molcom.nms.rolesandpriviledge.modulesandpriviledges.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto.ModulesModel;

import java.util.List;

public interface IModuleService {
    GenericResponse<ModulesModel> save(ModulesModel model, String roleType) throws Exception;

    GenericResponse<List<ModulesModel>> getAll() throws Exception;

    GenericResponse<ModulesModel> deleteById(int modulesId) throws Exception;
}
