package com.molcom.nms.assignAdminToApplication.controller;

import com.molcom.nms.assignAdminToApplication.dto.AssignAppToAdminBlkStep;
import com.molcom.nms.assignAdminToApplication.service.AssignAdminToApplicationService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/assignAdminToApplication")
public class AssignAdminToApplicationController {

    @Autowired
    private AssignAdminToApplicationService service;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to assign admin to application
     *
     * @param bulkUploadRequest
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody AssignAppToAdminBlkStep bulkUploadRequest,
                                   @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.bulkUpload(bulkUploadRequest);
        }

    }


    /**
     * Endpoint to get by id
     *
     * @param applicationId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getById")
    public GenericResponse<?> getById(@RequestParam String applicationId,
                                      @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.getByID(applicationId);
        }
    }

    /**
     * Endpoint to delete assignment
     *
     * @param assignAdminToAppId
     * @param authorization
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> delete(@RequestParam int assignAdminToAppId,
                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.deleteById(assignAdminToAppId);
        }
    }

    /**
     * Endpoint to get admins with a role
     *
     * @param roleName
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getAdminWithRole")
    public GenericResponse<?> getAdminWithRoles(@RequestParam String roleName,
                                                @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.getAdminWithRole(roleName);
        }
    }
}
