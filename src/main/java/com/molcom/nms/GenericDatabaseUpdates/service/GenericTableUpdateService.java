package com.molcom.nms.GenericDatabaseUpdates.service;


import com.molcom.nms.GenericDatabaseUpdates.repository.IGenericTableUpdateRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GenericTableUpdateService implements IGenericTableUpdateService {
    @Autowired
    IGenericTableUpdateRepository repository;


    /**
     * @param tableName
     * @param columnName
     * @param columnValue
     * @param columnConstraintKey
     * @param columnConstraintValue
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<String> updateTableColumn(String tableName, String columnName, String columnValue, String columnConstraintKey, String columnConstraintValue) throws Exception {
        GenericResponse<String> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.updateTableColumn(tableName, columnName, columnValue, columnConstraintKey, columnConstraintValue);
            log.info("AutoFeeResponse code {} ", responseCode);
            if (responseCode == 1 || responseCode == 2) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload("TRUE");
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

}
