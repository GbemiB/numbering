package com.molcom.nms.fee.schedule.service;

import com.molcom.nms.fee.schedule.dto.FeeScheduleModel;
import com.molcom.nms.fee.schedule.repository.IFeeScheduleRepo;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FeeScheduleService implements IFeeScheduleService {
    @Autowired
    private IFeeScheduleRepo repository;

    /**
     * Service implementation to add new fee schedule
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<FeeScheduleModel> save(FeeScheduleModel model) throws Exception {
        GenericResponse<FeeScheduleModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            int isExist = repository.isExist(model);
            if (isExist >= 1) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage("Fee schedule already exist");
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
        return genericResponse;
    }

    /**
     * Service implementation to edit existing fee schedule
     *
     * @param model
     * @param feeScheduleId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<FeeScheduleModel> edit(FeeScheduleModel model, int feeScheduleId) throws Exception {
        GenericResponse<FeeScheduleModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.edit(model, feeScheduleId);
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

    @Override
    public GenericResponse<FeeScheduleModel> deleteById(int feeScheduleId) throws Exception {
        GenericResponse<FeeScheduleModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteById(feeScheduleId);
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
     * Service implementation to find existing fee schedule by id
     *
     * @param feeScheduleId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<FeeScheduleModel> findById(int feeScheduleId) throws Exception {
        GenericResponse<FeeScheduleModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            FeeScheduleModel feeScheduleModel = repository.findById(feeScheduleId);
            log.info("Result set from repository {} ====> ", feeScheduleModel);
            if (feeScheduleModel != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(feeScheduleModel);
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
     * get all
     *
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<FeeScheduleModel>> getAll() throws Exception {
        GenericResponse<List<FeeScheduleModel>> genericResponse = new GenericResponse<>();
        try {
            List<FeeScheduleModel> feeScheduleModels = repository.getAll();
            log.info("Filtering: Result set from repository {} ====> ", feeScheduleModels);

            if (feeScheduleModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(feeScheduleModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!! {}", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    @Override
    public GenericResponse<List<FeeScheduleModel>> getShortCodeFee() throws Exception {
        GenericResponse<List<FeeScheduleModel>> genericResponse = new GenericResponse<>();
        try {
            List<FeeScheduleModel> feeScheduleModels = repository.getShortCodeFee();
            log.info("Filtering: Result set from repository {} ====> ", feeScheduleModels);

            if (feeScheduleModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(feeScheduleModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * Service implementation to filterForRegularUser existing fee schedules
     *
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param queryParam3
     * @param queryValue3
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<FeeScheduleModel>> filter(String queryParam1, String queryValue1,
                                                          String queryParam2, String queryValue2,
                                                          String queryParam3, String queryValue3,
                                                          String rowNumber) throws Exception {
        GenericResponse<List<FeeScheduleModel>> genericResponse = new GenericResponse<>();
        log.info("Query parameter for filtering {} ", queryParam1);
        try {
            List<FeeScheduleModel> feeScheduleModels = repository.filter(queryParam1, queryValue1,
                    queryParam2, queryValue2,
                    queryParam3, queryValue3,
                    rowNumber);
            log.info("Filtering: Result set from repository {} ====> ", feeScheduleModels);

            if (feeScheduleModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(feeScheduleModels);
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
