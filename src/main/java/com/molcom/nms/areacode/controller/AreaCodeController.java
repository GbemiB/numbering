package com.molcom.nms.areacode.controller;

import com.molcom.nms.areacode.dto.AreaCodeBlkReq;
import com.molcom.nms.areacode.dto.AreaCodeModel;
import com.molcom.nms.areacode.service.IAreaCodeService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/areaCode")
public class AreaCodeController {
    @Autowired
    private IAreaCodeService service;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to save new area code
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody AreaCodeModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.save(model);
        }

    }

    /**
     * Endpoint to save bulk area codes
     *
     * @param bulkUploadRequest
     * @return
     */
    @PostMapping("/bulkUpload")
    public GenericResponse<?> bulkUpload(@RequestBody AreaCodeBlkReq bulkUploadRequest,
                                         @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.bulkUpload(bulkUploadRequest);
        }
    }

    /**
     * Endpoint to edit existing area code
     *
     * @param model
     * @param areaId
     * @return
     * @throws Exception
     */
    @PatchMapping()
    public GenericResponse<?> edit(@RequestBody AreaCodeModel model,
                                   @RequestParam int areaId,
                                   @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.edit(model, areaId);
        }

    }

    /**
     * Endpoint to delete existing area code
     *
     * @param areaId
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> delete(@RequestParam int areaId,
                                     @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.deleteById(areaId);
        }
    }

    /**
     * Endpoint to get existing area code by id
     *
     * @param areaId
     * @return
     * @throws Exception
     */
    @GetMapping("/getByAreaId")
    public GenericResponse<?> getById(@RequestParam int areaId,
                                      @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.findById(areaId);
        }

    }

    /**
     * Endpoint to get all existing area codes
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/getAll")
    public GenericResponse<?> getAll(@RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.getAll();
        }
    }

    /**
     * Endpoint to get all existing area codes
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/getByCoverageArea")
    public GenericResponse<?> getAreaCodesByCoverageArea(String coverageArea,
                                                         @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.getAreaCodeByCoverageArea(coverageArea);
        }
    }

    /**
     * Endpoint to filterForRegularUser area codes, Filter:  AreaCode and CoverageArea
     *
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @GetMapping("/filter")
    public GenericResponse<?> filterCompRep(@RequestParam String queryParam1,
                                            @RequestParam String queryValue1,
                                            @RequestParam String queryParam2,
                                            @RequestParam String queryValue2,
                                            @RequestParam String rowNumber,
                                            @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.filter(queryParam1, queryValue1, queryParam2, queryValue2, rowNumber);
        }
    }
}
