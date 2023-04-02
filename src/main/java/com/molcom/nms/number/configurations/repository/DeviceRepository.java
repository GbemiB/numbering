package com.molcom.nms.number.configurations.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.number.configurations.dto.DeviceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class DeviceRepository implements IDeviceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Data layer to save device
     *
     * @param deviceModel
     * @return
     * @throws SQLException
     */
    @Override
    public int saveDevice(DeviceModel deviceModel) throws SQLException {
        String sql = "INSERT INTO Device (ApplicationId, DeviceName, ConnectionType, DeviceType, DeviceModel," +
                "Manufacturer,CreatedBy, CreatedDate ) " +
                "VALUES(?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                deviceModel.getApplicationId(),
                deviceModel.getDeviceName(),
                deviceModel.getConnectionType(),
                deviceModel.getDeviceType(),
                deviceModel.getDeviceModel(),
                deviceModel.getManufacturer(),
                deviceModel.getCreatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * Data layer to delete device
     *
     * @param deviceId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteDevice(String deviceId) throws SQLException {
        String sql = "DELETE FROM Device WHERE DeviceId = ?";
        return jdbcTemplate.update(sql, deviceId);
    }

    /**
     * Data layer to get device by application id
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<DeviceModel> getDevice(String applicationId) throws SQLException {
        String sql = "SELECT * FROM Device WHERE ApplicationId = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(DeviceModel.class), applicationId);
    }
}
