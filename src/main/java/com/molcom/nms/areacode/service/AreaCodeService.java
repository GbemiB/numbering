package com.molcom.nms.areacode.service;

import com.molcom.nms.areacode.dto.AreaCodeBlkReq;
import com.molcom.nms.areacode.dto.AreaCodeBlkRes;
import com.molcom.nms.areacode.dto.AreaCodeModel;
import com.molcom.nms.areacode.repository.IAreaCodeRepo;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AreaCodeService implements IAreaCodeService {

    @Autowired
    private IAreaCodeRepo repository;

    /**
     * Service implementation to save area code
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<AreaCodeModel> save(AreaCodeModel model) throws Exception {
        log.info("RequestBody", model);
        GenericResponse<AreaCodeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            int count = repository.areaCodeCount(model.getAreaCode());
            log.info("Checking if area code already exist {} ", count);
            if (count >= 1) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage("Area code already exist");
            } else {
                responseCode = repository.save(model);
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

    /**
     * Service implementation to save bulk area codes
     *
     * @param bulkUploadRequest
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<AreaCodeBlkRes> bulkUpload(AreaCodeBlkReq bulkUploadRequest) throws Exception {
        GenericResponse<AreaCodeBlkRes> genericResponse = new GenericResponse<>();
        try {
            AreaCodeBlkRes response = repository.bulkInsert(bulkUploadRequest);
            log.info("AreaCodeBlkRes {} ", response);
            if (response != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(response);
            } else {
                AreaCodeBlkRes resp = new AreaCodeBlkRes();
                resp.setTotalCount(String.valueOf(bulkUploadRequest.getBulkList().size()));
                resp.setBatchId(RefGenerator.getRefNo(5));
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                genericResponse.setOutputPayload(resp);
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!!! ", exception.getStackTrace());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to edit area code
     *
     * @param model
     * @param areaId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<AreaCodeModel> edit(AreaCodeModel model, int areaId) throws Exception {
        GenericResponse<AreaCodeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            int count = repository.areaCodeCount(model.getAreaCode());
            log.info("Checking if area code already exist {} ", count);
            if (count >= 1) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage("Area code already exist");
            } else {
                responseCode = repository.edit(model, areaId);
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
            log.info("Exception occurred !!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to delete area code
     *
     * @param areaId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<AreaCodeModel> deleteById(int areaId) throws Exception {
        GenericResponse<AreaCodeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteById(areaId);
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
     * Service implementation to get all area codes
     *
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<AreaCodeModel>> getAll() throws Exception {
        GenericResponse<List<AreaCodeModel>> genericResponse = new GenericResponse<>();
        try {
            List<AreaCodeModel> areaCodeModels = repository.getAll();
            log.info("Result set from repository {} ====> ", areaCodeModels);
            if (areaCodeModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(areaCodeModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception !!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * service implementation to by coverage area
     *
     * @param coverageArea
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<AreaCodeModel>> getAreaCodeByCoverageArea(String coverageArea) throws Exception {
        GenericResponse<List<AreaCodeModel>> genericResponse = new GenericResponse<>();
        try {
            List<AreaCodeModel> areaCodeModels = repository.getAreaCodeByCoverageArea(coverageArea);
            log.info("Result set from repository {} ====> ", areaCodeModels);
            if (areaCodeModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(areaCodeModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception !!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to get area code by id
     *
     * @param areaId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<AreaCodeModel> findById(int areaId) throws Exception {
        GenericResponse<AreaCodeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            AreaCodeModel areaCodeModel = repository.findById(areaId);
            log.info("Result set from repository {} ====> ", areaCodeModel);
            if (areaCodeModel != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(areaCodeModel);
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
     * Service implementation to filterForRegularUser area codes
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
    public GenericResponse<List<AreaCodeModel>> filter(String queryParam1, String queryValue1, String queryParam2, String queryValue2, String rowNumber) throws Exception {
        GenericResponse<List<AreaCodeModel>> genericResponse = new GenericResponse<>();
        log.info("Query parameter for filtering {} ", queryParam1);
        try {
            List<AreaCodeModel> areaCodeModels = repository.filter(queryParam1, queryValue1, queryParam2, queryValue2, rowNumber);
            log.info("Filtering: Result set from repository {} ====> ", areaCodeModels);

            if (areaCodeModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(areaCodeModels);
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
