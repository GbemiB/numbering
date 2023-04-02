package com.molcom.nms.numberCreation.special.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.numberCreation.special.dto.BulkUploadRequestSpecial;
import com.molcom.nms.numberCreation.special.dto.CreateSpecialNoModel;
import com.molcom.nms.numberCreation.special.service.CreateSpecialService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/createSpecialNumber")
public class CreateSpecialNumberController {

    @Autowired
    private CreateSpecialService service;

    @Autowired
    private TokenHandler tokenHandler;


    @PostMapping()
    public GenericResponse<?> save(@RequestBody CreateSpecialNoModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.createSingleNumber(model);
        }
    }

    @PostMapping("/bulkUpload")
    public GenericResponse<?> bulkUpload(@RequestBody BulkUploadRequestSpecial bulkUploadRequest,
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
    public GenericResponse<?> delete(@RequestParam String createdSpecialNumberId,
                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.deleteNumberById(createdSpecialNumberId);
        }
    }


    @GetMapping("/getByAccessCode")
    public GenericResponse<?> getByAccessCode(@RequestParam String accessCode,
                                              @RequestHeader("authorization") String authorization)
            throws Exception {

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
                                     @RequestParam String accessCode,
                                     @RequestParam String startDate,
                                     @RequestParam String endDate,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.filterNumber(numberSubType, accessCode, startDate, endDate, rowNumber);
        }
    }
}
