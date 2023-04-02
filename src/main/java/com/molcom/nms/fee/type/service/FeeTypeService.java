package com.molcom.nms.fee.type.service;

import com.molcom.nms.fee.type.dto.FeeTypeModel;
import com.molcom.nms.fee.type.repository.IFeeTypeRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FeeTypeService implements IFeeTypeService {

    @Autowired
    private IFeeTypeRepository repository;

    /**
     * Service implementation to save new fee type
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<FeeTypeModel> save(FeeTypeModel model) throws Exception {
        GenericResponse<FeeTypeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            int count = repository.feeTypeCount(model.getFeeTypeName());
            log.info("Checking if fee type already exist {} ", count);
            if (count >= 1) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage(ResponseStatus.ALREADY_EXIST.getMessage());
            } else {
                responseCode = repository.save(model);
                log.info("AutoFeeResponse code {} ", responseCode);
                if (responseCode == 1) {
                    genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                } else {
                    genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                }
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!! ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        log.info("AutoFeeResponse {}", genericResponse);
        return genericResponse;
    }

    /**
     * Service implementation to delete existing fee type by fee id
     *
     * @param feeTypeId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<FeeTypeModel> deleteById(int feeTypeId) throws Exception {
        GenericResponse<FeeTypeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteById(feeTypeId);
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
     * Service implementation to get all fee types
     *
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<FeeTypeModel>> getAll() throws Exception {
        GenericResponse<List<FeeTypeModel>> genericResponse = new GenericResponse<>();
        try {
            List<FeeTypeModel> coverageAreaModels = repository.getAll();
            log.info("Result set from repository {} ====> ", coverageAreaModels);
            if (coverageAreaModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(coverageAreaModels);
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
     * Service implementation to find fee type by fee id
     *
     * @param feeTypeId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<FeeTypeModel> findById(int feeTypeId) throws Exception {
        GenericResponse<FeeTypeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            FeeTypeModel feeTypeModel = repository.findById(feeTypeId);
            log.info("Result set from repository {} ====> ", feeTypeModel);
            if (feeTypeModel != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(feeTypeModel);
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
     * Service implementation to filterForRegularUser fee types by FeeName
     *
     * @param queryParam
     * @param queryValue
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<FeeTypeModel>> filter(String queryParam, String queryValue, String rowNumber) throws Exception {
        GenericResponse<List<FeeTypeModel>> genericResponse = new GenericResponse<>();
        log.info("Query parameter for filtering {} ", queryParam);
        try {
            List<FeeTypeModel> feeTypeModels = repository.filter(queryParam, queryValue, rowNumber);
            log.info("Filtering: Result set from repository {} ====> ", feeTypeModels);

            if (feeTypeModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(feeTypeModels);
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
