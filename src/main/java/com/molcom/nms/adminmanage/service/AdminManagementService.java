package com.molcom.nms.adminmanage.service;

import com.molcom.nms.adminmanage.dto.AdminManagementModel;
import com.molcom.nms.adminmanage.repository.IAdminManagementRepo;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AdminManagementService implements IAdminManagementService {

    @Autowired
    private IAdminManagementRepo repository;


    /**
     * Service implementation to save admin
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<AdminManagementModel> save(AdminManagementModel model) throws Exception {
        GenericResponse<AdminManagementModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.save(model);
            log.info("AutoFeeResponse code of Admin Management save ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while saving new Admin User {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to edit admin role
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<AdminManagementModel> editRole(AdminManagementModel model, int adminManageId) throws Exception {
        GenericResponse<AdminManagementModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.editRole(model, adminManageId);
            log.info("AutoFeeResponse code of Edit Role ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while editing Role {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to edit admin signature
     *
     * @param signature
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<AdminManagementModel> editSignature(String signature, int adminManageId) throws Exception {
        GenericResponse<AdminManagementModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.editSignature(signature, adminManageId);
            log.info("AutoFeeResponse code of Edit Signature ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while editing Signature {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Data layer to get all admins
     *
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<AdminManagementModel>> getAll(String rowNumber) throws Exception {
        GenericResponse<List<AdminManagementModel>> genericResponse = new GenericResponse<>();
        try {
            List<AdminManagementModel> adminManagementModels = repository.getAll(rowNumber);
            log.info("Result set from repository {} ====> ", adminManagementModels);
            if (adminManagementModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(adminManagementModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while filtering Admin Management {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Data layer to filter admins
     *
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param queryParam3
     * @param queryValue3
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<AdminManagementModel>> filter(String queryParam1, String queryValue1, String queryParam2, String queryValue2, String queryParam3, String queryValue3, String rowNumber) throws Exception {
        GenericResponse<List<AdminManagementModel>> genericResponse = new GenericResponse<>();
        log.info("Query parameter for filtering {} {} {} ", queryParam1, queryParam2, queryParam3);
        try {
            List<AdminManagementModel> adminManagement = repository.filter(queryParam1, queryValue1,
                    queryParam2, queryValue2, queryParam3, queryValue3, rowNumber);
            log.info("Filtering: Result set from repository {} ====> ", adminManagement);

            if (adminManagement != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(adminManagement);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while filtering Admin Management {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

}
