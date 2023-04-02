package com.molcom.nms.number.configurations.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.configurations.dto.FrequencyCoverageArea;
import com.molcom.nms.number.configurations.service.IFreqCoverageAreaService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/frequencyCoverage")
public class FrequencyCoverageAreaController {

    @Autowired
    private IFreqCoverageAreaService freqCoverageAreaService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to save frequency coverage area
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> saveFreqCoverageArea(@RequestBody FrequencyCoverageArea model,
                                                   @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return freqCoverageAreaService.saveFreqCoverageArea(model);
        }
    }

    /**
     * Endpoint to delete frequency coverage area
     *
     * @param frequencyCoverageId
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> deleteFreqCoverageArea(@RequestParam String frequencyCoverageId,
                                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return freqCoverageAreaService.deleteFreqCoverageArea(frequencyCoverageId);
        }
    }

    /**
     * Endpoint to get frequency coverage area by application Id
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @GetMapping()
    public GenericResponse<?> getByApplicationId(@RequestParam String applicationId,
                                                 @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return freqCoverageAreaService.getFreqCoverageArea(applicationId);
        }
    }
}
