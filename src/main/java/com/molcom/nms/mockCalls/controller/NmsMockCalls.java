package com.molcom.nms.mockCalls.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.mockCalls.dto.MockAllocationFeePayment;
import com.molcom.nms.mockCalls.dto.MockApplicationExpiration;
import com.molcom.nms.mockCalls.service.MockAllocationFeePaymentService;
import com.molcom.nms.mockCalls.service.MockApplicationExpirationService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/mockCalls")
public class NmsMockCalls {

    @Autowired
    private MockAllocationFeePaymentService mockAllocationFeePaymentService;

    @Autowired
    private MockApplicationExpirationService mockApplicationExpirationService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to mock allocation payment
     *
     * @param request
     * @param authorization
     * @return
     * @throws Exception
     */

    @PostMapping("/mockAllocationFeePayment")
    public GenericResponse<?> save(@RequestBody MockAllocationFeePayment request,
                                   @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return mockAllocationFeePaymentService.mockAllocationFeePayment(request);
        }
    }

    /**
     * Endpoint to mock expiration
     *
     * @param request
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping("/mockApplicationExpiration")
    public GenericResponse<?> save(@RequestBody MockApplicationExpiration request,
                                   @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return mockApplicationExpirationService.mockApplicationExpiration(request);
        }
    }
}


