package com.molcom.nms.approvals.controller;

import com.molcom.nms.approvals.dto.ApproveApplicationModel;
import com.molcom.nms.approvals.service.ApprovalService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/approval")
public class ApprovalController {

    @Autowired
    private ApprovalService service;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to save approval for an application step
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody ApproveApplicationModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.save(model);
        }
    }

    /**
     * Endpoint to get approval record for an application
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @GetMapping("/getApprovalRecord")
    public GenericResponse<?> getApprovalRecord(@RequestParam String applicationId,
                                                @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.getApprovalRecord(applicationId);
        }
    }

}
