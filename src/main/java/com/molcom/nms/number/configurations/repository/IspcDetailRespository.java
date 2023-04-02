package com.molcom.nms.number.configurations.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.number.configurations.dto.ISPCDetailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class IspcDetailRespository implements IIspcDetailRespository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Data layer to save ISPC details
     *
     * @param ispcDetailModel
     * @return
     * @throws SQLException
     */
    @Override
    public int saveIspcDetail(ISPCDetailModel ispcDetailModel) throws SQLException {
        String sql = "INSERT INTO IspcDetails (ApplicationId, SwitchName, SwitchType, SwitchFunction, SwitchLocation," +
                "CommissionDate, CreatedDate) " +
                "VALUES(?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                ispcDetailModel.getApplicationId(),
                ispcDetailModel.getSwitchName(),
                ispcDetailModel.getSwitchType(),
                ispcDetailModel.getSwitchFunction(),
                ispcDetailModel.getSwitchLocation(),
                ispcDetailModel.getCommissionDate(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * Data layer to delete ISPC details
     *
     * @param ispcDetailId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteIspcDetail(String ispcDetailId) throws SQLException {
        String sql = "DELETE FROM IspcDetails WHERE IspcDetailId = ?";
        return jdbcTemplate.update(sql, ispcDetailId);
    }

    /**
     * Data layer to get ISPC details by application id
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<ISPCDetailModel> getIspcDetail(String applicationId) throws SQLException {
        String sql = "SELECT * FROM IspcDetails WHERE ApplicationId = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(ISPCDetailModel.class), applicationId);
    }
}
