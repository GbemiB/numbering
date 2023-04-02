package com.molcom.nms.idManagement;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "nms/idManagement")
public class IdManagementController {

    @Autowired
    private IdManagementService idManagementService;

    @Autowired
    private TokenHandler tokenHandler;

    @GetMapping("/applicationId")
    public String getApplicationId(@RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return ResponseStatus.UNAUTHORIZED.getMessage();
        } else {
            return idManagementService.generateApplicationId();
        }

    }

    @GetMapping("/invoiceId")
    public String getInvoiceId(@RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return ResponseStatus.UNAUTHORIZED.getMessage();
        } else {
            return idManagementService.generateInvoiceId();
        }

    }
}
