package com.molcom.nms.sendBulkEmail.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.security.TokenHandler;
import com.molcom.nms.sendBulkEmail.dto.BulkEmailModel;
import com.molcom.nms.sendBulkEmail.service.IBulkEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "nms/bulkEmail")
public class BulkEmailController {

    @Autowired
    private IBulkEmailService bulkEmailService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * send bulk email
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody BulkEmailModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return bulkEmailService.sendEmail(model);
        }
    }

    @GetMapping("/getAll")
    public GenericResponse<?> getAll(@RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return bulkEmailService.getAll(rowNumber);
        }
    }


    /**
     * Filtering
     *
     * @param recipientEmail
     * @param mailSubject
     * @param mailSubject
     * @param startDate
     * @param endDate
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @GetMapping("/filter")
    public GenericResponse<?> filterCompRep(@RequestParam String recipientEmail,
                                            @RequestParam String mailSubject,
                                            @RequestParam String startDate,
                                            @RequestParam String endDate,
                                            @RequestParam String rowNumber,
                                            @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return bulkEmailService.filter(recipientEmail, mailSubject, startDate, endDate, rowNumber);
        }
    }
}
