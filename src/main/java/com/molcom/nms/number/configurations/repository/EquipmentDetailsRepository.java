package com.molcom.nms.number.configurations.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.number.configurations.dto.EquipmentDetailModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class EquipmentDetailsRepository implements IEquipmentDetailsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Data layer to add new equipment detail
     *
     * @param equipmentDetailModel
     * @return
     * @throws Exception
     */
    @Override
    public int saveEquipmentDetail(EquipmentDetailModel equipmentDetailModel) throws SQLException {
        String sql = "INSERT INTO EquipmentsDetails (ApplicationId, EquipmentName, EquipmentModel, EquipmentType," +
                " DateSelector, EquipmentManufacturer, CreatedDate) " +
                "VALUES(?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                equipmentDetailModel.getApplicationId(),
                equipmentDetailModel.getEquipmentName(),
                equipmentDetailModel.getEquipmentModel(),
                equipmentDetailModel.getEquipmentType(),
                equipmentDetailModel.getDateSelector(),
                equipmentDetailModel.getEquipmentManufacturer(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * Data layer to delete existing equipment detail
     *
     * @param equipmentId
     * @return
     * @throws Exception
     */
    @Override
    public int deleteEquipmentDetail(String equipmentId) throws SQLException {
        String sql = "DELETE FROM EquipmentsDetails WHERE EquipmentId = ?";
        return jdbcTemplate.update(sql, equipmentId);
    }

    /**
     * Data layer to get existing equipment detail by application id
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @Override
    public List<EquipmentDetailModel> getEquipmentDetail(String applicationId) throws SQLException {
        String sql = "SELECT * FROM EquipmentsDetails WHERE ApplicationId = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(EquipmentDetailModel.class), applicationId);
    }
}
