package com.molcom.nms.applicationPayment;


import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/updateApplicationPayment")
public class ApplicationPaymentController {

    @Autowired
    private ApplicationPaymentService service;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to update application payment
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> updateApplicationPayment(@RequestBody ApplicationPaymentRequest model,
                                                       @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.updateApplicationPayment(model);
        }
    }
}
