package com.molcom.nms.workflow.services;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.workflow.dto.WorkFlowModel;
import com.molcom.nms.workflow.dto.WorkflowObject;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IWorkFlowService {
    GenericResponse<WorkflowObject> save(WorkflowObject model) throws Exception;

    GenericResponse<WorkflowObject> saveForEdit(WorkflowObject model) throws Exception;

    GenericResponse<WorkFlowModel> getByID(int workflowId) throws Exception;

    GenericResponse<WorkFlowModel> getByWorkflowProcess(String process) throws Exception;

    GenericResponse<WorkflowObject> edit(WorkflowObject model, @RequestParam int workflowId) throws Exception;

    GenericResponse<WorkFlowModel> deleteById(int workflowId) throws Exception;

    GenericResponse<List<WorkFlowModel>> filter(String queryParam1, String queryValue1, String rowNumber) throws Exception;

}
