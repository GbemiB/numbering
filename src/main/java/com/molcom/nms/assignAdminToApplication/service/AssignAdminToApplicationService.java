package com.molcom.nms.assignAdminToApplication.service;

import com.molcom.nms.adminmanage.dto.AdminManagementModel;
import com.molcom.nms.adminmanage.repository.IAdminManagementRepo;
import com.molcom.nms.assignAdminToApplication.dto.AssignAdminToAppModel;
import com.molcom.nms.assignAdminToApplication.dto.AssignAppToAdminBlkRes;
import com.molcom.nms.assignAdminToApplication.dto.AssignAppToAdminBlkStep;
import com.molcom.nms.assignAdminToApplication.repository.AssignAdminToApplicationRepo;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AssignAdminToApplicationService {

    @Autowired
    private AssignAdminToApplicationRepo repository;

    @Autowired
    private IAdminManagementRepo adminManagementRepo;

    /**
     * Service implementation to get admin with roles
     *
     * @param roleName
     * @return
     * @throws Exception
     */
    public GenericResponse<List<AdminManagementModel>> getAdminWithRole(String roleName) throws Exception {
        GenericResponse<List<AdminManagementModel>> genericResponse = new GenericResponse<>();

        int responseCode = 0;
        try {
            List<AdminManagementModel> adminManagementModelList = adminManagementRepo.getAllAdminWithRole(roleName);
            log.info("AutoFeeResponse code ====== {} ", roleName);
            if (adminManagementModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(adminManagementModelList);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred !!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * Service implementation for bulk upload
     *
     * @param bulkUploadRequest
     * @return
     * @throws Exception
     */
    public GenericResponse<AssignAppToAdminBlkRes> bulkUpload(AssignAppToAdminBlkStep bulkUploadRequest) throws Exception {
        GenericResponse<AssignAppToAdminBlkRes> genericResponse = new GenericResponse<>();
        try {
            AssignAppToAdminBlkRes response = repository.bulkInsert(bulkUploadRequest);
            log.info("AssignAppToAdminBlkRes {} ", response);
            if (response != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(response);
            } else {
                AssignAppToAdminBlkRes resp = new AssignAppToAdminBlkRes();
                resp.setTotalCount(String.valueOf(bulkUploadRequest.getAssignApplication().size()));
                resp.setBatchId(RefGenerator.getRefNo(5));
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                genericResponse.setOutputPayload(resp);
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!!!! ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    public GenericResponse<List<AssignAdminToAppModel>> getByID(String applicationId) throws Exception {
        GenericResponse<List<AssignAdminToAppModel>> genericResponse = new GenericResponse<>();
        log.info("Id {} ", applicationId);
        try {
            List<AssignAdminToAppModel> assignAdminToAppModel = repository.getById(applicationId);
            log.info("Result set {} ====> ", assignAdminToAppModel);

            if (assignAdminToAppModel != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(assignAdminToAppModel);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to delete by id
     *
     * @param assignAdminToAppId
     * @return
     * @throws Exception
     */
    public GenericResponse<AssignAdminToAppModel> deleteById(int assignAdminToAppId) throws Exception {
        GenericResponse<AssignAdminToAppModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteById(assignAdminToAppId);
            log.info("AutoFeeResponse code ====== {} ", responseCode);
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
