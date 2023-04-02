package com.molcom.nms.accesscode.controller;


import com.molcom.nms.accesscode.dto.AccessCodeBlkReq;
import com.molcom.nms.accesscode.dto.AccessCodeModel;
import com.molcom.nms.accesscode.service.IAccessCodeService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/accessCode")
public class AccessCodeController {
    @Autowired
    private IAccessCodeService accessCodeService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to save new access code
     *
     * @param model as request body
     * @return response body as AccessCodeModel model
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody AccessCodeModel model,
                                   @RequestHeader("authorization") String authorization
    ) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return accessCodeService.save(model);
        }
    }

    /**
     * Endpoint to save bulk access codes
     *
     * @param bulkUploadRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/bulkUpload")
    public GenericResponse<?> bulkUpload(@RequestBody AccessCodeBlkReq bulkUploadRequest,
                                         @RequestHeader("authorization") String authorization)
            throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return accessCodeService.bulkInsert(bulkUploadRequest);
        }
    }

    /**
     * Endpoint to edit existing access code
     *
     * @param model        as request body
     * @param accessCodeId as request param
     * @return response body as AccessCodeModel
     * @throws Exception
     */
    @PatchMapping()
    public GenericResponse<?> edit(@RequestBody AccessCodeModel model,
                                   @RequestParam int accessCodeId,
                                   @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return accessCodeService.edit(model, accessCodeId);
        }

    }

    /**
     * Endpoint to delete existing access code
     *
     * @param accessCodeId as request param
     * @return AccessCodeModel as response body
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> delete(@RequestParam int accessCodeId,
                                     @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return accessCodeService.deleteById(accessCodeId);
        }
    }

    /**
     * Endpoint to get accessCode by access code id
     *
     * @param accessCodeId as request oaram
     * @return AccessCodeModel as response body
     * @throws Exception
     */
    @GetMapping("/getByAccessCodeId")
    public GenericResponse<?> getById(@RequestParam int accessCodeId,
                                      @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return accessCodeService.findById(accessCodeId);
        }


    }

    /**
     * Endpoint to get all access codes
     *
     * @return List<AccessCodeModel> as response body
     * @throws Exception
     */
    @GetMapping("getAll")
    public GenericResponse<?> getAll(@RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return accessCodeService.getAll();
        }
    }


    /**
     * Endpoint to get access codes by coverage area
     *
     * @param coverageArea
     * @return
     * @throws Exception
     */
    @GetMapping("/getByCoverageArea")
    public GenericResponse<?> getByCoverageArea(String coverageArea,
                                                @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return accessCodeService.getByCoverageName(coverageArea);
        }

    }

    /**
     * Endpoint to get access codes by area code
     *
     * @param areaCode
     * @return
     * @throws Exception
     */
    @GetMapping("/getByAreaCode")
    public GenericResponse<?> getByAreaCode(String areaCode,
                                            @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return accessCodeService.getByAreaCode(areaCode);
        }
    }

    /**
     * Endpoint to get access codes by number subtypes
     *
     * @param numberSubType
     * @return
     * @throws Exception
     */
    @GetMapping("/getByNumberSubType")
    public GenericResponse<?> getByNumberSubType(String numberSubType,
                                                 @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return accessCodeService.getByNumberSubType(numberSubType);
        }
    }

    /**
     * Endpoint to filterForRegularUser access codes: Filters are AccessCode, CoverageArea, AreaCode, and Status
     *
     * @param queryParam1 as param key one
     * @param queryValue1 as param value one
     * @param queryParam2 as param key two
     * @param queryValue2 as param value two
     * @param queryParam3 as param key three
     * @param queryValue2 as param value three
     * @param rowNumber   as result count
     * @return List<AccessCodeModel> as response body
     * @throws Exception
     */

    @GetMapping("/filter")
    public GenericResponse<?> filterAccessCode(@RequestParam String queryParam1,
                                               @RequestParam String queryValue1,
                                               @RequestParam String queryParam2,
                                               @RequestParam String queryValue2,
                                               @RequestParam String queryParam3,
                                               @RequestParam String queryValue3,
                                               @RequestParam String queryParam4,
                                               @RequestParam String queryValue4,
                                               @RequestParam String rowNumber,
                                               @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return accessCodeService.filter(queryParam1, queryValue1, queryParam2, queryValue2,
                    queryParam3, queryValue3, queryParam4, queryValue4, rowNumber);
        }

    }


}
