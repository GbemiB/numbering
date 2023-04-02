package com.molcom.nms.adminmanage.controller;

import com.molcom.nms.adminmanage.dto.AdminManagementModel;
import com.molcom.nms.adminmanage.service.AdminManagementService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/adminmanagement")
public class AdminManagementController {

    @Autowired
    private AdminManagementService adminManagementService;


    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to save new Admin User
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody AdminManagementModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return adminManagementService.save(model);
        }

    }

    @GetMapping()
    public GenericResponse<?> getAll(@RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return adminManagementService.getAll(rowNumber);
        }
    }


    /**
     * Endpoint to edit role assigned to the admin User
     *
     * @param model
     * @param adminManageId
     * @return
     * @throws Exception
     */
    @PatchMapping()
    public GenericResponse<?> editRole(@RequestBody AdminManagementModel model,
                                       @RequestParam int adminManageId,
                                       @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return adminManagementService.editRole(model, adminManageId);
        }
    }


    @PatchMapping("/signature")
    public GenericResponse<?> editSignature(@RequestBody String signature,
                                            @RequestParam int adminManageId,
                                            @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return adminManagementService.editSignature(signature, adminManageId);
        }
    }

    /**
     * Endpoint to filter Admin Management
     * Filters are Name, Email and Role
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

    @GetMapping("/filter")
    public GenericResponse<?> filter(@RequestParam String queryParam1,
                                     @RequestParam String queryValue1,
                                     @RequestParam String queryParam2,
                                     @RequestParam String queryValue2,
                                     @RequestParam String queryParam3,
                                     @RequestParam String queryValue3,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return adminManagementService.filter(queryParam1, queryValue1, queryParam2, queryValue2, queryParam3,
                    queryValue3, rowNumber);
        }
    }
}
