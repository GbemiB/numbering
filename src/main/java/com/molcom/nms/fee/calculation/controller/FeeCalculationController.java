package com.molcom.nms.fee.calculation.controller;

import com.molcom.nms.fee.calculation.dto.GenerateShortCodeInvoiceReq;
import com.molcom.nms.fee.calculation.service.FeeCalculatorShortCodeNew;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/feeCalculator")
public class FeeCalculationController {
    @Autowired
    private FeeCalculatorShortCodeNew feeCalculatorShortCode;

    @Autowired
    private TokenHandler tokenHandler;


    /**
     * Endpoint to calculate short code allocation
     *
     * @param request
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> calculateShortCodeAllocation(@RequestBody GenerateShortCodeInvoiceReq request,
                                                           @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return feeCalculatorShortCode.generateShortCodeAllocation(request);
        }
    }

}
