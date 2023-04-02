package com.molcom.nms.eservicesintegrations.controller;

import com.molcom.nms.eservicesintegrations.dto.AuthUserResponse;
import com.molcom.nms.eservicesintegrations.dto.DirectPaymentRequest;
import com.molcom.nms.eservicesintegrations.service.AuthService;
import com.molcom.nms.eservicesintegrations.service.PaymentService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/eServices/integrations")
public class IntegrationController {

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private AuthService authService;

    @Autowired
    private PaymentService paymentService;

    /**
     * Endpoint for authentication (login)
     *
     * @param token
     * @return
     * @throws Exception
     */
    @GetMapping("/auth")
    public GenericResponse<AuthUserResponse> authUser(@RequestParam String token) throws Exception {
        return authService.authenticate(token);
    }

    /**
     * Endpoint to carry out direct payment
     *
     * @param directPaymentRequest
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping("/direct-payment")
    public GenericResponse<?> doPayment(@RequestBody DirectPaymentRequest directPaymentRequest,
                                        @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return paymentService.directPaymentCall(directPaymentRequest);
        }
    }

    /**
     * Endpoint to check payment status
     *
     * @param transactionRef
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/check-payment-status")
    public GenericResponse<?> checkStatus(@RequestParam String transactionRef,
                                          @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return paymentService.checkPaymentStatus(transactionRef);
        }
    }
}
