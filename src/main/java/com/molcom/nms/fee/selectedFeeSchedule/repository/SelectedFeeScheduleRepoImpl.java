package com.molcom.nms.fee.selectedFeeSchedule.repository;

import com.molcom.nms.fee.selectedFeeSchedule.dto.SelectedFeeScheduleModel;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class SelectedFeeScheduleRepoImpl implements ISelectedFeeScheduleRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int save(SelectedFeeScheduleModel model) throws SQLException {
        String sql = "INSERT INTO FeeSelected (ApplicationId, FeeAmount, FeeName, FeeDescription, InvoiceType, CreatedDate) " +
                "VALUES(?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                model.getApplicationId(),
                model.getFeeAmount(),
                model.getFeeName(),
                model.getFeeDescription(),
                model.getInvoiceType(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<SelectedFeeScheduleModel> findByApplicationId(String applicationId) throws SQLException {
        String sql = "SELECT * from FeeSelected WHERE ApplicationId=?";
        List<SelectedFeeScheduleModel> selectedFeeScheduleModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SelectedFeeScheduleModel.class), applicationId);
        return selectedFeeScheduleModels;
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<SelectedFeeScheduleModel> findByApplicationIdNewFee(String applicationId) throws SQLException {
        String sql = "SELECT * from FeeSelected WHERE ApplicationId=? AND UPPER(InvoiceType)='NEW'";
        List<SelectedFeeScheduleModel> selectedFeeScheduleModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SelectedFeeScheduleModel.class), applicationId);
        return selectedFeeScheduleModels;
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<SelectedFeeScheduleModel> findByApplicationIdRenewalFee(String applicationId) throws SQLException {
        String sql = "SELECT * from FeeSelected WHERE ApplicationId=? AND UPPER(InvoiceType)='RENEWAL'";
        List<SelectedFeeScheduleModel> selectedFeeScheduleModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SelectedFeeScheduleModel.class), applicationId);
        return selectedFeeScheduleModels;
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<SelectedFeeScheduleModel> findByApplicationIdApplictaionFee(String applicationId) throws SQLException {
        String sql = "SELECT * from FeeSelected WHERE ApplicationId=? AND UPPER(InvoiceType)='APPLICATION'";
        List<SelectedFeeScheduleModel> selectedFeeScheduleModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SelectedFeeScheduleModel.class), applicationId);
        return selectedFeeScheduleModels;
    }
}
