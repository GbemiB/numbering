package com.molcom.nms.numberReport.ispc.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.numberReport.ispc.dto.BulkUploadReportIspc;
import com.molcom.nms.numberReport.ispc.service.ReportIspcService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/reportIspcNumber")
public class ReportIspcNumberController {

    @Autowired
    private ReportIspcService service;

    @Autowired
    private TokenHandler tokenHandler;


    /**
     * @param bulkUploadRequest
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping("/bulkUpload")
    public GenericResponse<?> bulkUpload(@RequestBody BulkUploadReportIspc bulkUploadRequest,
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
     * @param startDate
     * @param endDate
     * @param organisation
     * @param rowNumber
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/filter")
    public GenericResponse<?> filter(@RequestParam String startDate,
                                     @RequestParam String endDate,
                                     @RequestParam String organisation,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.filterNumber(startDate, endDate, organisation, rowNumber);
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

