package com.molcom.nms.numberCreation.standard.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.numberCreation.standard.dto.BulkUploadRequestStandard;
import com.molcom.nms.numberCreation.standard.dto.CreateStandardNoModel;
import com.molcom.nms.numberCreation.standard.service.CreateStandardNoService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/createStandardNumber")
public class CreateStandardNumberController {

    @Autowired
    private CreateStandardNoService service;

    @Autowired
    private TokenHandler tokenHandler;


    @PostMapping()
    public GenericResponse<?> save(@RequestBody CreateStandardNoModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.createSingleNumber(model);
        }
    }

    @PostMapping("/bulkUpload")
    public GenericResponse<?> bulkUpload(@RequestBody BulkUploadRequestStandard bulkUploadRequest,
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
    public GenericResponse<?> delete(@RequestParam String createdStandardNumberId,
                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.deleteNumberById(createdStandardNumberId);
        }
    }


    @GetMapping("/getByAccessCode")
    public GenericResponse<?> getByAccessCode(@RequestParam String accessCode,
                                              @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.getNumberByAccessCode(accessCode);
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
    public GenericResponse<?> filter(@RequestParam String numberSubType,
                                     @RequestParam String coverageArea,
                                     @RequestParam String areaCode,
                                     @RequestParam String accessCode,
                                     @RequestParam String startDate,
                                     @RequestParam String endDate,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.filterNumber(numberSubType, coverageArea, areaCode, accessCode, startDate, endDate, rowNumber);
        }
    }
}
