package com.molcom.nms.number.configurations.repository;

import com.molcom.nms.number.configurations.dto.DeviceModel;

import java.sql.SQLException;
import java.util.List;

public interface IDeviceRepository {
    int saveDevice(DeviceModel deviceModel) throws SQLException;

    int deleteDevice(String deviceId) throws SQLException;

    List<DeviceModel> getDevice(String applicationId) throws SQLException;
}
