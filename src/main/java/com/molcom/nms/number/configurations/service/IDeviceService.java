package com.molcom.nms.number.configurations.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.number.configurations.dto.DeviceModel;

import java.util.List;

public interface IDeviceService {
    GenericResponse<DeviceModel> saveDevice(DeviceModel deviceModel) throws Exception;

    GenericResponse<DeviceModel> deleteDevice(String deviceId) throws Exception;

    GenericResponse<List<DeviceModel>> getDevice(String applicationId) throws Exception;
}
