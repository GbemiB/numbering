package com.molcom.nms.rolesandpriviledge.userroles.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto.BulkUploadRequestModule;
import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto.BulkUploadResponseModel;
import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.repository.IModuleRepo;
import com.molcom.nms.rolesandpriviledge.userroles.dto.UserObjectModel;
import com.molcom.nms.rolesandpriviledge.userroles.dto.UserRoleModel;
import com.molcom.nms.rolesandpriviledge.userroles.repository.IUserRoleRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserRoleService implements IUserRoleService {

    @Autowired
    private IUserRoleRepo userRepo;

    @Autowired
    private IModuleRepo moduleRepo;


    /**
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<UserObjectModel> save(UserObjectModel model) throws Exception {
        GenericResponse<UserObjectModel> genericResponse = new GenericResponse<>();
        int roleResponseCode = 0;
        try {
            int count = userRepo.doesRoleExist(model.getUserRoles().getRoleType());
            if (count <= 0) {
                roleResponseCode = userRepo.save(model.getUserRoles());
                BulkUploadResponseModel modulesResponse = moduleRepo.bulkInsert(model.getAssignedModule(), model.getUserRoles().getRoleType());

                log.info("AutoFeeResponse code {} ", roleResponseCode);
                if (roleResponseCode == 1 && modulesResponse != null) {
                    genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                } else {
                    genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                }
            } else {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage(ResponseStatus.ALREADY_EXIST.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!!!! {}", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<UserObjectModel> replace(UserObjectModel model) throws Exception {
        GenericResponse<UserObjectModel> genericResponse = new GenericResponse<>();
        int saveRoleResponseCode = 0;
        int deleteUserResponseCode = 0;
        int deleteModuleResponseCode = 0;
        try {

            // Delete the role and the linked modules
            assert model.getUserRoles().getUserTypeId() != null;
            deleteUserResponseCode = userRepo.deleteById(Integer.parseInt(model.getUserRoles().getUserTypeId()));
            deleteModuleResponseCode = moduleRepo.deleteByRoleType(model.getUserRoles().getRoleType());

            if (deleteModuleResponseCode != 0 && deleteModuleResponseCode != 0) {
                // Resave the role and modules
                saveRoleResponseCode = userRepo.edit(model.getUserRoles());
                BulkUploadResponseModel modulesResponse = moduleRepo.bulkInsert(model.getAssignedModule(),
                        model.getUserRoles().getRoleType());

                log.info("Save role response code {}", saveRoleResponseCode);
                log.info("Save modules response code {}", modulesResponse);

                log.info("AutoFeeResponse code {} ", saveRoleResponseCode);
                if (saveRoleResponseCode == 1 && modulesResponse != null) {
                    genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                } else {
                    genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                }
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!!!! {}", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param userRoleId
     * @param userRoleType
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<UserRoleModel> deleteById(int userRoleId, String userRoleType) throws Exception {
        GenericResponse<UserRoleModel> genericResponse = new GenericResponse<>();
        int userResponseCode = 0;
        int moduleResponseCode = 0;
        try {
            userResponseCode = userRepo.deleteById(userRoleId);
            moduleResponseCode = moduleRepo.deleteByRoleType(userRoleType);

            log.info("AutoFeeResponse code  {} {}", userResponseCode, moduleResponseCode);
            if (userResponseCode == 1) {
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


    /**
     * @param userRoleId
     * @param userRoleType
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<UserObjectModel> findById(int userRoleId, String userRoleType) throws Exception {
        GenericResponse<UserObjectModel> genericResponse = new GenericResponse<>();
        UserObjectModel res = new UserObjectModel();
        BulkUploadRequestModule list = new BulkUploadRequestModule();
        try {
            list.setModuleList(moduleRepo.getModuleByRoleType(userRoleType));

            res.setUserRoles(userRepo.findById(userRoleId));
            res.setAssignedModule(list);

            if (userRepo.findById(userRoleId) != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(res);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred !!!!", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param userRoleType
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<UserObjectModel> findByRoleName(String userRoleType) throws Exception {
        GenericResponse<UserObjectModel> genericResponse = new GenericResponse<>();
        UserObjectModel res = new UserObjectModel();
        BulkUploadRequestModule list = new BulkUploadRequestModule();
        try {
            list.setModuleList(moduleRepo.getModuleByRoleType(userRoleType));

            res.setUserRoles(userRepo.findByRoleName(userRoleType));
            res.setAssignedModule(list);

            if (userRepo.findByRoleName(userRoleType) != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(res);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred !!!!", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<UserRoleModel>> getAllRows(String rowNumber) throws Exception {
        GenericResponse<List<UserRoleModel>> genericResponse = new GenericResponse<>();
        try {
            List<UserRoleModel> userRoleModels = userRepo.getAllRoles(rowNumber);
            log.info("Filtering: Result set from userRepo {} ====> ", userRoleModels);

            if (userRoleModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(userRoleModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param queryParam
     * @param queryValue
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<UserRoleModel>> filter(String queryParam, String queryValue, String rowNumber) throws Exception {
        GenericResponse<List<UserRoleModel>> genericResponse = new GenericResponse<>();
        log.info("Query parameter for filtering {} ", queryParam);
        try {
            List<UserRoleModel> userRoleModels = userRepo.filter(queryParam, queryValue, rowNumber);
            log.info("Filtering: Result set from userRepo {} ====> ", userRoleModels);

            if (userRoleModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(userRoleModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

}
