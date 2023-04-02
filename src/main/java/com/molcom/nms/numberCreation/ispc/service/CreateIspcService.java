package com.molcom.nms.numberCreation.ispc.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.numberCreation.ispc.dto.BulkUploadRequestIspc;
import com.molcom.nms.numberCreation.ispc.dto.BulkUploadResponse;
import com.molcom.nms.numberCreation.ispc.dto.CreateIspcNoModel;
import com.molcom.nms.numberCreation.ispc.repository.CreateIspcRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CreateIspcService {
    @Autowired
    private CreateIspcRepository repository;

    public GenericResponse<CreateIspcNoModel> createSingleNumber(CreateIspcNoModel model) {
        log.info("RequestBody", model);
        GenericResponse<CreateIspcNoModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            int count = repository.countIfNoExist(model.getIspcNumber());
            log.info("Checking if number already exist {} ", count);
            if (count >= 1) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage("ISPC number already exist");
            } else {
                responseCode = repository.createSingleNumber(model);
                log.info("AutoFeeResponse code for saving new area code {} ", responseCode);
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
        log.info("AutoFeeResponse", genericResponse);
        return genericResponse;
    }


    public GenericResponse<BulkUploadResponse> createBulkNumber(BulkUploadRequestIspc bulkUploadRequest) {
        GenericResponse<BulkUploadResponse> genericResponse = new GenericResponse<>();
        try {
            BulkUploadResponse response = repository.createBulkNumber(bulkUploadRequest);
            log.info("Bulk upload {} ", response);
            if (response != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(response);
            } else {
                BulkUploadResponse resp = new BulkUploadResponse();
                resp.setTotalCount(String.valueOf(bulkUploadRequest.getBulkList().size()));
                resp.setBatchId(RefGenerator.getRefNo(5));
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                genericResponse.setOutputPayload(resp);
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!!!! ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    public GenericResponse<CreateIspcNoModel> deleteNumberById(String id) {
        GenericResponse<CreateIspcNoModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteNumberById(id);
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


    public GenericResponse<CreateIspcNoModel> getNumberById(String id) {
        GenericResponse<CreateIspcNoModel> genericResponse = new GenericResponse<>();
        CreateIspcNoModel response;
        try {
            response = repository.getNumberById(id);
            log.info("AutoFeeResponse payload ====== {} ", response);
            if (response != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(response);
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

    public GenericResponse<List<CreateIspcNoModel>> getAllNumber() {
        GenericResponse<List<CreateIspcNoModel>> genericResponse = new GenericResponse<>();
        try {
            List<CreateIspcNoModel> createIspcNoModelList = repository.getAllNumber();
            log.info("Result set from repository {} ====> ", createIspcNoModelList);
            if (createIspcNoModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(createIspcNoModelList);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    public GenericResponse<List<CreateIspcNoModel>> filterNumber(String startDate, String endDate, String rowNumber) {
        GenericResponse<List<CreateIspcNoModel>> genericResponse = new GenericResponse<>();
        try {
            List<CreateIspcNoModel> createIspcNoModelList = repository.filterNumber(startDate, endDate, rowNumber);
            log.info("Result set from repository {} ====> ", createIspcNoModelList);
            if (createIspcNoModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(createIspcNoModelList);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }
}
