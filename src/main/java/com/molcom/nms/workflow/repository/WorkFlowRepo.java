package com.molcom.nms.workflow.repository;

import com.molcom.nms.workflow.dto.WorkFlowModel;
import com.molcom.nms.workflow.dto.WorkFlowStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class WorkFlowRepo implements IWorkFlowRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int save(WorkFlowModel model) throws SQLException {
        String exist = "SELECT COUNT(*) FROM WorkFlow WHERE NameOfWorkFlow = ? OR Process = ?";
        log.info("Exist {}", exist);
        int doesExist = jdbcTemplate.queryForObject(exist, new Object[]{model.getNameOfWorkFlow(), model.getProcess()}, Integer.class);
        if (doesExist > 0)
            return -1;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO WorkFlow (NameOfWorkflow, Process) " +
                "VALUES(?,?)";

        int numOfAffectedRecords = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"WorkFlowId"});
            ps.setString(1, model.getNameOfWorkFlow());
            ps.setString(2, model.getProcess());
            return ps;
        }, keyHolder);

        Integer workFlowId = keyHolder.getKey().intValue();
        String sql2 = "INSERT INTO WorkFlowStep (WorkFlowId, WorkFlowStepName, WorkFlowStepNum, GenerateInvoice, Roles) " +
                "VALUES(?,?,?,?,?)";
        List<WorkFlowStep> workflowSteps = model.getSteps();
        workflowSteps.forEach(o -> {
            String roles = o.getRoles().stream().collect(Collectors.joining(","));
            jdbcTemplate.update(sql2, workFlowId, o.getWorkFlowStepName(),
                    o.getWorkFlowStepNum(),
                    o.getGenerateInvoice(),
                    roles);
        });

        return numOfAffectedRecords;

    }

    /**
     * @param workflowId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteById(int workflowId) throws SQLException {
        // delete workflow
        String sql = "DELETE FROM WorkFlow WHERE WorkFlowId = ?";
        int responseCode = jdbcTemplate.update(sql, workflowId);
        if (responseCode == 1) {
            // delete related steps
            deleteWorkflowStep(workflowId);
        }
        return responseCode;
    }

    /**
     * @param workflowId
     * @return
     * @throws SQLException
     */
    private int deleteWorkflowStep(int workflowId) throws SQLException {
        String sql = "DELETE FROM WorkFlowStep WHERE WorkFlowId = ?";
        return jdbcTemplate.update(sql, workflowId);
    }

    /**
     * @param workflowId
     * @return
     * @throws SQLException
     */
    @Override
    public WorkFlowModel getById(int workflowId) throws SQLException {

        String sql = "SELECT * from WorkFlow WHERE  WorkFlowId =?";
        try {
            // getting workflow
            WorkFlowModel workFlowModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(WorkFlowModel.class), workflowId);
            log.info("WorkFlowModel {} ", workFlowModel);

            // getting steps
            String sql2 = "SELECT * from WorkFlowStep WHERE WorkFlowId = ?";
            List<WorkFlowStep> steps = jdbcTemplate.query(sql2, new Object[]{workflowId}, BeanPropertyRowMapper.newInstance(WorkFlowStep.class));
            workFlowModel.setSteps(steps);

            return workFlowModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred while getting signatory by id");
            return null;
        }
    }

    /**
     * @param process
     * @return
     * @throws SQLException
     */
    @Override
    public WorkFlowModel getByProcess(String process) throws SQLException {
        String sql = "SELECT * from WorkFlow WHERE  Process =?";
        try {
            // getting workflow
            WorkFlowModel workFlowModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(WorkFlowModel.class), process);

            // getting steps
            String sql2 = "SELECT * from WorkFlowStep WHERE WorkFlowId = ?";
            List<WorkFlowStep> steps = jdbcTemplate.query(sql2, new Object[]{workFlowModel.getWorkflowId()}, BeanPropertyRowMapper.newInstance(WorkFlowStep.class));
            workFlowModel.setSteps(steps);

            return workFlowModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred!!!");
            return null;
        }
    }

    /**
     * @param queryParam1
     * @param queryValue1
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<WorkFlowModel> filter(String queryParam1, String queryValue1, String rowNumber) throws SQLException {
        String sql = "SELECT * from WorkFlow WHERE  " + queryParam1 + "  LIKE '%" + queryValue1 + "%' " + "ORDER BY " + queryParam1 + " ASC LIMIT " + rowNumber + "";
        log.info(sql);
        List<WorkFlowModel> workFlowModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(WorkFlowModel.class));
        String sql2 = "SELECT * from WorkFlowStep WHERE WorkFlowId = ?";
        log.info(sql2);
        workFlowModels.forEach(e -> {
            List<WorkFlowStep> steps = jdbcTemplate.query(sql2, new Object[]{e.getWorkflowId()}, BeanPropertyRowMapper.newInstance(WorkFlowStep.class));
            e.setSteps(steps);
        });
        log.info("WorkFlowModel {} ", workFlowModels);
        return workFlowModels;
    }
}

