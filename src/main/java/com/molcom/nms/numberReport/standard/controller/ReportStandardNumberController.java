package com.molcom.nms.numberReport.standard.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.numberReport.standard.dto.BulkUploadReportStandard;
import com.molcom.nms.numberReport.standard.service.ReportStandardNoService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/reportStandardNumber")
public class ReportStandardNumberController {

    @Autowired
    private ReportStandardNoService service;

    @Autowired
    private TokenHandler tokenHandler;


    /**
     * @param bulkUploadRequest
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping("/bulkUpload")
    public GenericResponse<?> bulkUpload(@RequestBody BulkUploadReportStandard bulkUploadRequest,
                                         @RequestHeader("authorization") String authorization)
            throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.createBulkNumber(bulkUploadRequest);
        }
    }

    /**
     * @param organisation
     * @param numberSubType
     * @param coverageArea
     * @param areaCode
     * @param accessCode
     * @param startDate
     * @param endDate
     * @param rowNumber
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/filter")
    public GenericResponse<?> filter(@RequestParam String organisation,
                                     @RequestParam String numberSubType,
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
            return service.filterNumber(organisation, numberSubType, coverageArea, areaCode, accessCode, startDate, endDate, rowNumber);
        }
    }

    /**
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getAllForAdmin")
    public GenericResponse<?> getAll(@RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.getAll();
        }
    }

    /**
     * @param organisation
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getAllForNonAdmin")
    public GenericResponse<?> getAllFor(@RequestParam String organisation,
                                        @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.getByOrganisation(organisation);
        }
    }

}
