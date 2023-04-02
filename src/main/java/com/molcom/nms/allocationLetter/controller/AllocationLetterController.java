package com.molcom.nms.allocationLetter.controller;

import com.molcom.nms.allocationLetter.dto.AllocationLetter;
import com.molcom.nms.allocationLetter.service.AllocationLetterService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/allocationLeter")
public class AllocationLetterController {


    @Autowired
    private AllocationLetterService service;


    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to get details for building allocation letter
     *
     * @param applicationId
     * @param numberType
     * @return
     * @throws Exception
     */
    @GetMapping("/getDetailForAllocationLetter")
    public GenericResponse<?> generateAllocationLetter(@RequestParam String applicationId,
                                                       @RequestParam String numberType,
                                                       @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.getDetailsForAllocationLetter(applicationId, numberType);
        }

    }

    /**
     * Endpoint to save link to built allocation letter
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping("/saveAllocationLetter")
    public GenericResponse<?> saveAllocationLetter(@RequestBody AllocationLetter model,
                                                   @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.saveAllocationLetter(model);
        }

    }

}
