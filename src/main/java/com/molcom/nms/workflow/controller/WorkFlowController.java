package com.molcom.nms.workflow.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.security.TokenHandler;
import com.molcom.nms.workflow.dto.WorkflowObject;
import com.molcom.nms.workflow.services.WorkFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/workflow")
public class WorkFlowController {
    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * @param obj
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody WorkflowObject obj,
                                   @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return workFlowService.save(obj);
        }

    }

    /**
     * @param obj
     * @param workflowId
     * @param authorization
     * @return
     * @throws Exception
     */
    @PatchMapping()
    public GenericResponse<?> edit(@RequestBody WorkflowObject obj, @RequestParam int workflowId,
                                   @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return workFlowService.edit(obj, workflowId);
        }
    }

    /**
     * @param workflowId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getById")
    public GenericResponse<?> getById(@RequestParam int workflowId,
                                      @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return workFlowService.getByID(workflowId);
        }
    }

    /**
     * @param workflowName
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getByProcess")
    public GenericResponse<?> getByProcess(@RequestParam String workflowName,
                                           @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return workFlowService.getByWorkflowProcess(workflowName);
        }
    }

    /**
     * @param workflowId
     * @param authorization
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> delete(@RequestParam int workflowId,
                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return workFlowService.deleteById(workflowId);
        }
    }

    /**
     * @param queryParam1
     * @param queryValue1
     * @param rowNumber
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/filter")
    public GenericResponse<?> filter(@RequestParam String queryParam1,
                                     @RequestParam String queryValue1,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return workFlowService.filter(queryParam1, queryValue1, rowNumber);
        }
    }
}
