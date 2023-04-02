package com.molcom.nms.billingcycle.service;

import com.molcom.nms.billingcycle.dto.BillingCycleModel;
import com.molcom.nms.billingcycle.repository.BillingCycleRepo;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BillingCycleService implements IBillingCycleService {

    @Autowired
    private BillingCycleRepo repository;


    /**
     * Service implementation to save billing cycle
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<BillingCycleModel> save(BillingCycleModel model) throws Exception {
        GenericResponse<BillingCycleModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            int isExit = repository.isExist(model.getBillingName());
            if (isExit >= 1) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage("Billing cycle already exist");
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
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to edit billing cycle
     *
     * @param model
     * @param billingId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<BillingCycleModel> edit(BillingCycleModel model, int billingId) throws Exception {
        GenericResponse<BillingCycleModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.edit(model, billingId);
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
     * Service implementation to delete by id billing cycle
     *
     * @param billingId
     * @return
     * @throws Exception
     */

    @Override
    public GenericResponse<BillingCycleModel> deleteById(int billingId) throws Exception {
        GenericResponse<BillingCycleModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteById(billingId);
            log.info("AutoFeeResponse code ====== {} ", responseCode);
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
     * Service implementation to get all billing cycle
     *
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<BillingCycleModel>> getAll() throws Exception {
        GenericResponse<List<BillingCycleModel>> genericResponse = new GenericResponse<>();
        try {
            List<BillingCycleModel> billingCycleModels = repository.getAll();
            log.info("Result set from repository {} ====> ", billingCycleModels);
            if (billingCycleModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(billingCycleModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while filtering BillingCycle {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to find billing cycle by id
     *
     * @param billingId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<BillingCycleModel> findById(int billingId) throws Exception {
        GenericResponse<BillingCycleModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            BillingCycleModel billingCycleModel = repository.findById(billingId);
            log.info("Result set from repository {} ====> ", billingCycleModel);
            if (billingCycleModel != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(billingCycleModel);
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
     * Service implementation to filter billing cycle
     *
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<BillingCycleModel>> filter(String queryParam1, String queryValue1, String queryParam2, String queryValue2, String rowNumber) throws Exception {
        GenericResponse<List<BillingCycleModel>> genericResponse = new GenericResponse<>();
        log.info("Query parameter for filtering Billing Cycle {} ", queryParam2);
        try {
            List<BillingCycleModel> billingCycleModels = repository.filter(queryParam1, queryValue1, queryParam2, queryValue2, rowNumber);
            log.info("Filtering: Result set from repository {} ====> ", billingCycleModels);

            if (billingCycleModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(billingCycleModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while filtering Billing Cycle {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

}