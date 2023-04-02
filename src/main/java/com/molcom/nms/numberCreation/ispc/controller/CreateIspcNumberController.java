package com.molcom.nms.numberCreation.ispc.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.numberCreation.ispc.dto.BulkUploadRequestIspc;
import com.molcom.nms.numberCreation.ispc.dto.CreateIspcNoModel;
import com.molcom.nms.numberCreation.ispc.service.CreateIspcService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/createIspcNumber")
public class CreateIspcNumberController {

    @Autowired
    private CreateIspcService service;

    @Autowired
    private TokenHandler tokenHandler;


    @PostMapping()
    public GenericResponse<?> save(@RequestBody CreateIspcNoModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.createSingleNumber(model);
        }

    }

    @PostMapping("/bulkUpload")
    public GenericResponse<?> bulkUpload(@RequestBody BulkUploadRequestIspc bulkUploadRequest,
                                         @RequestHeader("authorization") String authorization)
            throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.createBulkNumber(bulkUploadRequest);
        }
    }


    @DeleteMapping()
    public GenericResponse<?> delete(@RequestParam String createdIspcNumberId,
                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.deleteNumberById(createdIspcNumberId);
        }

    }


    @GetMapping("/getById")
    public GenericResponse<?> getById(@RequestParam String createdIspcNumberId,
                                      @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.getNumberById(createdIspcNumberId);
        }
    }


    @GetMapping("getAll")
    public GenericResponse<?> getAll(@RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.getAllNumber();
        }
    }


    @GetMapping("/filter")
    public GenericResponse<?> filter(@RequestParam String startDate,
                                     @RequestParam String endDate,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.filterNumber(startDate, endDate, rowNumber);
        }
    }
}
