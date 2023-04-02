package com.molcom.nms.workflow.repository;

import com.molcom.nms.workflow.dto.WorkFlowModel;

import java.sql.SQLException;
import java.util.List;

public interface IWorkFlowRepo {
    int save(WorkFlowModel model) throws SQLException;

    int deleteById(int workflowId) throws SQLException;

    WorkFlowModel getById(int workflowId) throws SQLException;

    WorkFlowModel getByProcess(String process) throws SQLException;

    List<WorkFlowModel> filter(String queryParam1, String queryValue1,
                               String rowNumber) throws SQLException;
}
