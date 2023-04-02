package com.molcom.nms.numberCreation.shortcode.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.numberCreation.shortcode.dto.BulkUploadExistingShortCode;
import com.molcom.nms.numberCreation.shortcode.dto.BulkUploadRequestShortCode;
import com.molcom.nms.numberCreation.shortcode.dto.CreateShortCodeNoModel;
import com.molcom.nms.numberCreation.shortcode.service.CreateShortCodeService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/createShortCodeNumber")
public class CreateShortCodeNumberController {

    @Autowired
    private CreateShortCodeService service;

    @Autowired
    private TokenHandler tokenHandler;


    @PostMapping()
    public GenericResponse<?> save(@RequestBody CreateShortCodeNoModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.createSingleNumber(model);
        }
    }

    @PostMapping("/bulkUpload")
    public GenericResponse<?> bulkUpload(@RequestBody BulkUploadRequestShortCode bulkUploadRequest,
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
    public GenericResponse<?> delete(@RequestParam String createdShortCodeNumberId,
                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.deleteNumberById(createdShortCodeNumberId);
        }
    }


    @GetMapping("/getByService")
    public GenericResponse<?> getByService(@RequestParam String serviceName,
                                           @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.getNumberByService(serviceName);
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
    public GenericResponse<?> filter(@RequestParam String shortCodeCategory,
                                     @RequestParam String shortCodeService,
                                     @RequestParam String startDate,
                                     @RequestParam String endDate,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.filterNumber(shortCodeCategory, shortCodeService, startDate, endDate, rowNumber);
        }
    }

    @PostMapping("/uploadExistingShortCode")
    public GenericResponse<?> uploadExistingShortCode(@RequestBody BulkUploadExistingShortCode bulkUploadRequest,
                                                      @RequestHeader("authorization") String authorization)
            throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.uploadExistingBulkUpload(bulkUploadRequest);
        }
    }
}
