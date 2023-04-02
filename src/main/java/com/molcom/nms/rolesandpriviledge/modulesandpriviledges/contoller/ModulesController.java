package com.molcom.nms.rolesandpriviledge.modulesandpriviledges.contoller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto.ModulesModel;
import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.service.IModuleService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/modules")
public class ModulesController {

    @Autowired
    private IModuleService moduleService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping()
    public GenericResponse<?> getAll(@RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return moduleService.getAll();
        }
    }

    /**
     * @param model
     * @param roleType
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody ModulesModel model, String roleType,
                                   @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return moduleService.save(model, roleType);
        }
    }

    /**
     * @param modulesId
     * @param authorization
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> deleteById(@RequestParam int modulesId,
                                         @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return moduleService.deleteById(modulesId);
        }
    }
}
