package com.molcom.nms.numberReport.special.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.numberReport.special.dto.BulkUploadReportSpecial;
import com.molcom.nms.numberReport.special.service.ReportSpecialService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/reportSpecialNumber")
public class ReportSpecialNumberController {

    @Autowired
    private ReportSpecialService service;

    @Autowired
    private TokenHandler tokenHandler;


    /**
     * @param bulkUploadRequest
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping("/bulkUpload")
    public GenericResponse<?> bulkUpload(@RequestBody BulkUploadReportSpecial bulkUploadRequest,
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
                                     @RequestParam String accessCode,
                                     @RequestParam String startDate,
                                     @RequestParam String endDate,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.filterNumber(organisation, numberSubType, accessCode, startDate, endDate, rowNumber);
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
