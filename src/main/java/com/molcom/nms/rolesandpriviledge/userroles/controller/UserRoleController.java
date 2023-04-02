package com.molcom.nms.rolesandpriviledge.userroles.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.rolesandpriviledge.userroles.dto.UserObjectModel;
import com.molcom.nms.rolesandpriviledge.userroles.service.IUserRoleService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/userRole")
public class UserRoleController {
    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * @param model
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody UserObjectModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return userRoleService.save(model);
        }
    }

    /**
     * @param model
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping("/edit")
    public GenericResponse<?> replace(@RequestBody UserObjectModel model,
                                      @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return userRoleService.replace(model);
        }
    }

    /**
     * @param userRoleId
     * @param userRoleType
     * @param authorization
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> delete(@RequestParam int userRoleId, @RequestParam String userRoleType,
                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return userRoleService.deleteById(userRoleId, userRoleType);
        }
    }

    /**
     * @param userTypeId
     * @param userRoleType
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getByuserId")
    public GenericResponse<?> getById(@RequestParam int userTypeId, @RequestParam String userRoleType,
                                      @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return userRoleService.findById(userTypeId, userRoleType);
        }
    }

    /**
     * @param userRoleType
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getByRoleName")
    public GenericResponse<?> getByRoleName(@RequestParam String userRoleType,
                                            @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return userRoleService.findByRoleName(userRoleType);
        }
    }

    /**
     * @param queryParam
     * @param queryValue
     * @param rowNumber
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/filter")
    public GenericResponse<?> filterRole(@RequestParam String queryParam,
                                         @RequestParam String queryValue,
                                         @RequestParam String rowNumber,
                                         @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return userRoleService.filter(queryParam, queryValue, rowNumber);
        }
    }

    /**
     * @param rowNumber
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getAllRoles")
    public GenericResponse<?> getAllRoles(
            @RequestParam String rowNumber,
            @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return userRoleService.getAllRows(rowNumber);
        }
    }
}

