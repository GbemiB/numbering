package com.molcom.nms.numberCreation.availableNumbers.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.numberCreation.availableNumbers.dto.GetAvailableNumber;
import com.molcom.nms.numberCreation.availableNumbers.dto.GetAvailableNumberCount;
import com.molcom.nms.numberCreation.availableNumbers.service.AvailableNoCountService;
import com.molcom.nms.numberCreation.availableNumbers.service.AvailableNumbersService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/availableNumbers")
public class AvailableNumbersController {

    @Autowired
    private AvailableNumbersService availableNumbersService;

    @Autowired
    private AvailableNoCountService availableNoCountService;

    @Autowired
    private TokenHandler tokenHandler;

    @PostMapping()
    public GenericResponse<?> getAvailableNumber(@RequestBody GetAvailableNumber model,
                                                 @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return availableNumbersService.getAvailableNumbers(model);
        }

    }

    @PostMapping("/count")
    public GenericResponse<?> getAvailableNumberCount(@RequestBody GetAvailableNumberCount model,
                                                      @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return availableNoCountService.getAvailableNumbers(model);
        }

    }
}
