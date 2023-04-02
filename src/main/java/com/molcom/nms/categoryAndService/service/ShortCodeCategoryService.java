package com.molcom.nms.categoryAndService.service;

import com.molcom.nms.categoryAndService.dto.ShortCodeCategoryModel;
import com.molcom.nms.categoryAndService.repository.ShortCodeCategoryRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShortCodeCategoryService {
    @Autowired
    private ShortCodeCategoryRepository repository;


    /**
     * Service to save short code categories
     *
     * @param model
     * @return
     * @throws Exception
     */
    public GenericResponse<ShortCodeCategoryModel> save(ShortCodeCategoryModel model) throws Exception {
        GenericResponse<ShortCodeCategoryModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            int isExit = repository.isExist(model.getCategoryName());
            if (isExit >= 1) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage("Category already exist");
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
     * service to get all
     *
     * @return
     * @throws Exception
     */
    public GenericResponse<List<ShortCodeCategoryModel>> getAll() throws Exception {
        GenericResponse<List<ShortCodeCategoryModel>> genericResponse = new GenericResponse<>();
        try {
            List<ShortCodeCategoryModel> shortCodeCategoryModels = repository.getAll();
            log.info("Result set from repository {} ====> ", shortCodeCategoryModels);

            if (shortCodeCategoryModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(shortCodeCategoryModels);
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
     * service to delete
     *
     * @param categoryName
     * @return
     * @throws Exception
     */
    public GenericResponse<ShortCodeCategoryModel> delete(String categoryName) throws Exception {
        GenericResponse<ShortCodeCategoryModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteById(categoryName);
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
