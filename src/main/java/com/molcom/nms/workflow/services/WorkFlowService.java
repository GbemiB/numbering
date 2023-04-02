package com.molcom.nms.workflow.services;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.workflow.dto.WorkFlowModel;
import com.molcom.nms.workflow.dto.WorkflowObject;
import com.molcom.nms.workflow.repository.WorkFlowRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
public class WorkFlowService implements IWorkFlowService {

    @Autowired
    private WorkFlowRepo repository;

    /**
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<WorkflowObject> save(WorkflowObject model) throws Exception {
        GenericResponse<WorkflowObject> genericResponse = new GenericResponse<>();
        log.info("Workflow object {}", model);

        if (model.getProcess() == null || model.getProcess().isEmpty()) {
            WorkFlowModel emptyProcess = new WorkFlowModel();
            emptyProcess.setNameOfWorkFlow(model.getNameOfWorkFlow());
            emptyProcess.setProcess("");
            emptyProcess.setSteps(model.getSteps());
            emptyProcess.setWorkflowId("");
            try {
                int res = repository.save(emptyProcess);
                log.info("save workflow without process {}", res);
                if (res == 1) {
                    genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                } else if (res == -1) {
                    genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.ALREADY_EXIST.getMessage());
                } else {
                    genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            model.getProcess().forEach(processVal -> {
                WorkFlowModel mapped = new WorkFlowModel();
                mapped.setNameOfWorkFlow(processVal + " " + model.getNameOfWorkFlow());
                mapped.setSteps(model.getSteps());
                mapped.setProcess(processVal);
                mapped.setWorkflowId("");
                try {
                    int res = repository.save(mapped);
                    log.info("save workflow {}", res);
                    if (res == 1) {
                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                    } else if (res == -1) {
                        genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.ALREADY_EXIST.getMessage());
                    } else {
                        genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
        return genericResponse;
    }

    /**
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<WorkflowObject> saveForEdit(WorkflowObject model) throws Exception {
        GenericResponse<WorkflowObject> genericResponse = new GenericResponse<>();
        log.info("Workflow object {}", model);

        if (model.getProcess() == null || model.getProcess().isEmpty()) {
            WorkFlowModel emptyProcess = new WorkFlowModel();
            emptyProcess.setNameOfWorkFlow(model.getNameOfWorkFlow());
            emptyProcess.setProcess("");
            emptyProcess.setSteps(model.getSteps());
            emptyProcess.setWorkflowId("");
            try {
                int res = repository.save(emptyProcess);
                log.info("save workflow without process {}", res);
                if (res == 1) {
                    genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                } else if (res == -1) {
                    genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.ALREADY_EXIST.getMessage());
                } else {
                    genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            model.getProcess().forEach(processVal -> {
                WorkFlowModel mapped = new WorkFlowModel();
                mapped.setNameOfWorkFlow(model.getNameOfWorkFlow());
                mapped.setSteps(model.getSteps());
                mapped.setProcess(processVal);
                mapped.setWorkflowId("");
                try {
                    int res = repository.save(mapped);
                    log.info("save workflow {}", res);
                    if (res == 1) {
                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                    } else if (res == -1) {
                        genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.ALREADY_EXIST.getMessage());
                    } else {
                        genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
        return genericResponse;
    }

    /**
     * @param workflowId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<WorkFlowModel> getByID(int workflowId) throws Exception {
        GenericResponse<WorkFlowModel> genericResponse = new GenericResponse<>();
        log.info("Workflow id set {} ", workflowId);
        try {
            WorkFlowModel workFlowModel = repository.getById(workflowId);
            log.info("Result set {} ====> ", workFlowModel);

            if (workFlowModel != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(workFlowModel);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param process
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<WorkFlowModel> getByWorkflowProcess(String process) throws Exception {
        GenericResponse<WorkFlowModel> genericResponse = new GenericResponse<>();
        log.info("Workflow process  {} ", process);
        try {
            WorkFlowModel workFlowModel = repository.getByProcess(process);
            log.info("Result set {} ====> ", workFlowModel);

            if (workFlowModel != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(workFlowModel);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param model
     * @param workflowId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<WorkflowObject> edit(WorkflowObject model, @RequestParam int workflowId) throws Exception {
        GenericResponse<WorkflowObject> genericResponse = new GenericResponse<>();
        int responseCode = 1;
        responseCode = repository.deleteById(workflowId);
        if (responseCode == 1) {
            genericResponse = saveForEdit(model);
        }
        return genericResponse;
    }


    /**
     * @param workflowId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<WorkFlowModel> deleteById(int workflowId) throws Exception {
        GenericResponse<WorkFlowModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteById(workflowId);
            log.info("AutoFeeResponse code ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param queryParam1
     * @param queryValue1
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<WorkFlowModel>> filter(String queryParam1, String queryValue1, String rowNumber) throws Exception {
        GenericResponse<List<WorkFlowModel>> genericResponse = new GenericResponse<>();
        log.info("Query parameter for filtering {} ", queryParam1);
        try {
            List<WorkFlowModel> workFlowModels = repository.filter(queryParam1, queryValue1, rowNumber);
            log.info("Filtering: Result set from repository {} ====> ", workFlowModels);

            if (workFlowModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(workFlowModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while filtering Work Flow {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }
}
