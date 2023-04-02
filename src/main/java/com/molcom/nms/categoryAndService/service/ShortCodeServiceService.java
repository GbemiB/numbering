package com.molcom.nms.categoryAndService.service;

import com.molcom.nms.categoryAndService.dto.ShortCodeServiceModel;
import com.molcom.nms.categoryAndService.repository.ShortCodeServiceRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShortCodeServiceService {
    @Autowired
    private ShortCodeServiceRepository repository;

    /**
     * save
     *
     * @param model
     * @return
     * @throws Exception
     */
    public GenericResponse<ShortCodeServiceModel> save(ShortCodeServiceModel model) throws Exception {
        GenericResponse<ShortCodeServiceModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            int isExit = repository.isExist(model.getServiceName());
            if (isExit >= 1) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage("Service already exist");
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
            log.info("Exception occurred {} ", exception.getMessage());
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
    public GenericResponse<List<ShortCodeServiceModel>> getAll() throws Exception {
        GenericResponse<List<ShortCodeServiceModel>> genericResponse = new GenericResponse<>();
        try {
            List<ShortCodeServiceModel> shortCodeServiceModels = repository.getAll();
            log.info("Result set from repository {} ====> ", shortCodeServiceModels);

            if (shortCodeServiceModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(shortCodeServiceModels);
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

    /**
     * get by category name
     *
     * @param categoryName
     * @return
     * @throws Exception
     */
    public GenericResponse<List<ShortCodeServiceModel>> getByCategoryName(String categoryName) throws Exception {
        GenericResponse<List<ShortCodeServiceModel>> genericResponse = new GenericResponse<>();
        try {
            List<ShortCodeServiceModel> shortCodeServiceModels = repository.getByCategoryName(categoryName);
            log.info("Result set from repository {} ====> ", shortCodeServiceModels);

            if (shortCodeServiceModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(shortCodeServiceModels);
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

    /**
     * delete
     *
     * @param serviceName
     * @param shortCodeCategory
     * @return
     * @throws Exception
     */
    public GenericResponse<ShortCodeServiceModel> delete(String serviceName, String shortCodeCategory) throws Exception {
        GenericResponse<ShortCodeServiceModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteById(serviceName, shortCodeCategory);
            log.info("AutoFeeResponse code {} ", responseCode);
            if (responseCode >= 1) {
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
}

