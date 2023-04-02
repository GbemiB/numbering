package com.molcom.nms.number.configurations.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.configurations.dto.DeviceModel;
import com.molcom.nms.number.configurations.repository.IDeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DeviceService implements IDeviceService {
    @Autowired
    private IDeviceRepository repository;

    /**
     * Service implementation to save new device
     *
     * @param deviceModel
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<DeviceModel> saveDevice(DeviceModel deviceModel) throws Exception {
        GenericResponse<DeviceModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveDevice(deviceModel);
            log.info("AutoFeeResponse code {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to delete existing device
     *
     * @param deviceId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<DeviceModel> deleteDevice(String deviceId) throws Exception {
        GenericResponse<DeviceModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteDevice(deviceId);
            log.info("AutoFeeResponse code ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to get existing device by application id
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<DeviceModel>> getDevice(String applicationId) throws Exception {
        GenericResponse<List<DeviceModel>> genericResponse = new GenericResponse<>();
        try {
            List<DeviceModel> deviceModels = repository.getDevice(applicationId);
            log.info("Result set from repository {} ====> ", deviceModels);

            if (deviceModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(deviceModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }
}

