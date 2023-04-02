package com.molcom.nms.coveragearea.controller;

import com.molcom.nms.coveragearea.dto.BulkUploadRequest;
import com.molcom.nms.coveragearea.dto.CoverageAreaModel;
import com.molcom.nms.coveragearea.service.CoverageAreaService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/coverageArea")
public class CoverageAreaController {

    @Autowired
    private CoverageAreaService coverageAreaService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to save new coverage area
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody CoverageAreaModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return coverageAreaService.save(model);
        }
    }

    /**
     * Endpoint to upload bulk coverage areas
     *
     * @param bulkUploadRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/bulkUpload")
    public GenericResponse<?> bulkUpload(@RequestBody BulkUploadRequest bulkUploadRequest,
                                         @RequestHeader("authorization") String authorization)
            throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return coverageAreaService.bulkUpload(bulkUploadRequest);
        }
    }

    /**
     * Endpoint to edit existing coverage area
     *
     * @param model
     * @param coverageId
     * @return
     * @throws Exception
     */
    @PatchMapping()
    public GenericResponse<?> edit(@RequestBody CoverageAreaModel model,
                                   @RequestParam int coverageId,
                                   @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return coverageAreaService.edit(model, coverageId);
        }
    }

    /**
     * Endpoint to delete existing coverage area
     *
     * @param coverageId
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> delete(@RequestParam int coverageId,
                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return coverageAreaService.deleteById(coverageId);
        }
    }

    /**
     * Endpoint to get existing coverage area by id
     *
     * @param coverageId
     * @return
     * @throws Exception
     */
    @GetMapping("/getByCoverageId")
    public GenericResponse<?> getById(@RequestParam int coverageId,
                                      @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return coverageAreaService.findById(coverageId);
        }
    }

    @GetMapping("/getByCoverageType")
    public GenericResponse<?> getByType(@RequestParam String coverageType,
                                        @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return coverageAreaService.getCoverageType(coverageType);
        }
    }

    /**
     * Endpoint to get all coverage areas
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
            return coverageAreaService.getAll();
        }
    }

    /**
     * Endpoint to filterForRegularUser existing coverage areas
     * Filters: CoverageName and CoverageType
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
                                            @RequestHeader("authorization") String authorization
    ) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return coverageAreaService.filter(queryParam1, queryValue1, queryParam2, queryValue2, rowNumber);
        }
    }
}
