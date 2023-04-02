package com.molcom.nms.numbertype.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.numbertype.dto.NumberTypeModel;
import com.molcom.nms.numbertype.repository.NumberTypeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NuberTypeService implements INumberTypeService {

    @Autowired
    private NumberTypeRepo repository;


    /**
     * @param model
     * @param numberTypeId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<NumberTypeModel> update(NumberTypeModel model, int numberTypeId) throws Exception {
        GenericResponse<NumberTypeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.update(model, numberTypeId);
            log.info("AutoFeeResponse code  {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<NumberTypeModel>> getAll() throws Exception {
        GenericResponse<List<NumberTypeModel>> genericResponse = new GenericResponse<>();
        try {
            List<NumberTypeModel> numberTypeModels = repository.getAll();
            log.info("Result set from repository {} ====> ", numberTypeModels);
            if (numberTypeModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(numberTypeModels);
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

    /**
     * @param numberTypeId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<NumberTypeModel> findById(int numberTypeId) throws Exception {
        GenericResponse<NumberTypeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;

        try {
            NumberTypeModel numberTypeModel = repository.findById(numberTypeId);
            log.info("Result set from repository {} ====> ", numberTypeModel);
            if (numberTypeModel != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(numberTypeModel);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred !!!!", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param queryParam
     * @param queryValue
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<NumberTypeModel>> filter(String queryParam, String queryValue, String rowNumber) throws Exception {
        GenericResponse<List<NumberTypeModel>> genericResponse = new GenericResponse<>();
        log.info("Query parameter for filtering {} ", queryParam);
        try {
            List<NumberTypeModel> numberTypeModels = repository.filter(queryParam, queryValue, rowNumber);
            log.info("Filtering: Result set from repository {} ====> ", numberTypeModels);

            if (numberTypeModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(numberTypeModels);
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
