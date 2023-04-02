package com.molcom.nms.fee.selectedFeeSchedule.service;

import com.molcom.nms.fee.selectedFeeSchedule.dto.SelectedFeeScheduleModel;
import com.molcom.nms.fee.selectedFeeSchedule.repository.ISelectedFeeScheduleRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class ISelectedFeeScheduleServiceImpl implements ISelectedFeeScheduleService {
    @Autowired
    private ISelectedFeeScheduleRepository repository;

    /**
     * save selected fee schedule
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<SelectedFeeScheduleModel> save(SelectedFeeScheduleModel model) throws SQLException {
        GenericResponse<SelectedFeeScheduleModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.save(model);
            log.info("AutoFeeResponse code {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * find selected fee by application id
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<List<SelectedFeeScheduleModel>> findByApplicationId(String applicationId) throws SQLException {
        GenericResponse<List<SelectedFeeScheduleModel>> genericResponse = new GenericResponse<>();
        try {
            List<SelectedFeeScheduleModel> models = repository.findByApplicationId(applicationId);

            if (models != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(models);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while filtering company representative {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }
}
