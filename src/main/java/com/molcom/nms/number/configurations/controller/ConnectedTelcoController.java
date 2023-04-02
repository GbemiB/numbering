package com.molcom.nms.number.configurations.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.configurations.dto.ConnectedTelcoCompany;
import com.molcom.nms.number.configurations.service.IConnectdTelcoCompService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping(path = "nms/connectedTelcoCompany")
public class ConnectedTelcoController {

    @Autowired
    private IConnectdTelcoCompService connectdTelcoCompService;

    @Autowired
    private TokenHandler tokenHandler;


    /**
     * Endpoint to add connected telecommunication
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> saveConnectedTelcoComp(@RequestBody ConnectedTelcoCompany model,
                                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return connectdTelcoCompService.saveConnectedTelcoCompanies(model);
        }
    }


    /**
     * Endpoint to delete connected telecommunication
     *
     * @param interconnectId
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> deleteConnectedTelcoComp(@RequestParam String interconnectId,
                                                       @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return connectdTelcoCompService.deleteConnectedTelcoCompanies(interconnectId);
        }
    }

    /**
     * Endpoint to get connected telecommunication by application id
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @GetMapping()
    public GenericResponse<?> getByApplicationId(@RequestParam String applicationId,
                                                 @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return connectdTelcoCompService.getConnectedTelcoCompanies(applicationId);
        }
    }


}
