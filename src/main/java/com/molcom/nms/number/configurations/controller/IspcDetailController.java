package com.molcom.nms.number.configurations.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.configurations.dto.ISPCDetailModel;
import com.molcom.nms.number.configurations.service.IIspcDetailService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/ispcDetails")
public class IspcDetailController {


    @Autowired
    private IIspcDetailService ispcDetailService;

    @Autowired
    private TokenHandler tokenHandler;


    /**
     * Endpoint to add selected number
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> saveIspcDetail(@RequestBody ISPCDetailModel model,
                                             @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return ispcDetailService.saveIspcDetails(model);
        }
    }


    /**
     * Endpoint to delete selected number
     *
     * @param ispcDetailId
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> deleteIspcDetail(@RequestParam String ispcDetailId,
                                               @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return ispcDetailService.deleteIspcDetails(ispcDetailId);
        }
    }


    /**
     * Endpoint to get selected numbers by application id
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @GetMapping()
    public GenericResponse<?> getIspcDetailByApplicationId(@RequestParam String applicationId,
                                                           @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return ispcDetailService.getIspcDetails(applicationId);
        }
    }

}
