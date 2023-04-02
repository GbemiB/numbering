package com.molcom.nms.rolesandpriviledge.modulesandpriviledges.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto.ModulesModel;
import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.repository.IModuleRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ModulesService implements IModuleService {

    @Autowired
    private IModuleRepo repository;

    /**
     * @param model
     * @param roleType
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<ModulesModel> save(ModulesModel model, String roleType) throws Exception {
        GenericResponse<ModulesModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.save(model, roleType);
            log.info("AutoFeeResponse code {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!! {}", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<ModulesModel>> getAll() throws Exception {
        GenericResponse<List<ModulesModel>> genericResponse = new GenericResponse<>();
        try {
            List<ModulesModel> models = repository.getAll();

            if (models != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(models);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!{} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * @param modulesId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<ModulesModel> deleteById(int modulesId) throws Exception {
        GenericResponse<ModulesModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteById(modulesId);
            log.info("AutoFeeResponse code  {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

}
