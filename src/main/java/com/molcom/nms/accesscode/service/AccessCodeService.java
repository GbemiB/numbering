package com.molcom.nms.accesscode.service;

import com.molcom.nms.accesscode.dto.AccessCodeBlkReq;
import com.molcom.nms.accesscode.dto.AccessCodeBlkRes;
import com.molcom.nms.accesscode.dto.AccessCodeModel;
import com.molcom.nms.accesscode.repository.IAccessCodeRepo;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
public class AccessCodeService implements IAccessCodeService {
    @Autowired
    private IAccessCodeRepo repository;

    /**
     * Service implementation to save new access code
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<AccessCodeModel> save(AccessCodeModel model) throws Exception {
        GenericResponse<AccessCodeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            int count = repository.accessCodeCount(model.getAccessCodeName());
            log.info("Checking if access code already exist {} ", count);
            if (count >= 1) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage("Access code already exist");
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
            log.info("Exception occurred !!!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to edit existing access code
     *
     * @param model
     * @param accessCodeId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<AccessCodeModel> edit(AccessCodeModel model, int accessCodeId) throws Exception {
        GenericResponse<AccessCodeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        int count = 0;
        try {
            if (count >= 1) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage("Access code already exist");
            } else {
                responseCode = repository.edit(model, accessCodeId);
                log.info("AutoFeeResponse code  {} ", responseCode);
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
     * Service implementation to delete existing access code
     *
     * @param accessCodeId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<AccessCodeModel> deleteById(int accessCodeId) throws Exception {
        GenericResponse<AccessCodeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteById(accessCodeId);
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
     * Service implementation to get all existing access codes
     *
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<AccessCodeModel>> getAll() throws Exception {
        GenericResponse<List<AccessCodeModel>> genericResponse = new GenericResponse<>();
        try {
            List<AccessCodeModel> accessCodeModels = repository.getAll();
            log.info("Result set from repository {} ====> ", accessCodeModels);
            if (accessCodeModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(accessCodeModels);
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
     * Service implementation to filterForRegularUser existing access codes
     *
     * @param accessCodeId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<AccessCodeModel> findById(int accessCodeId) throws Exception {
        GenericResponse<AccessCodeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            AccessCodeModel accessCodeModel = repository.findById(accessCodeId);
            log.info("Result set from repository {} ====> ", accessCodeModel);
            if (accessCodeModel != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(accessCodeModel);
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
     * Service implementation to get access code by coverage area
     *
     * @param coverageArea
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<AccessCodeModel>> getByCoverageName(String coverageArea) throws Exception {
        GenericResponse<List<AccessCodeModel>> genericResponse = new GenericResponse<>();
        try {
            List<AccessCodeModel> accessCodeModels = repository.getByCoverageName(coverageArea);
            log.info("Result set from repository {} ====> ", accessCodeModels);
            if (accessCodeModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(accessCodeModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!  {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to get access code by area code
     *
     * @param areaCode
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<AccessCodeModel>> getByAreaCode(String areaCode) throws Exception {
        GenericResponse<List<AccessCodeModel>> genericResponse = new GenericResponse<>();
        try {
            List<AccessCodeModel> accessCodeModels = repository.getByAreaCode(areaCode);
            log.info("Result set from repository {} ====> ", accessCodeModels);
            if (accessCodeModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(accessCodeModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!  {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to get access code by number subtype
     *
     * @param numberSubType
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<AccessCodeModel>> getByNumberSubType(String numberSubType) throws Exception {
        GenericResponse<List<AccessCodeModel>> genericResponse = new GenericResponse<>();
        try {
            List<AccessCodeModel> accessCodeModels = repository.getByNumberSubType(numberSubType);
            log.info("Result set from repository {} ====> ", accessCodeModels);
            if (accessCodeModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(accessCodeModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!  {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implmementation for bulk upload of access codes
     *
     * @param bulkUploadRequest
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<AccessCodeBlkRes> bulkInsert(AccessCodeBlkReq bulkUploadRequest) throws SQLException {
        GenericResponse<AccessCodeBlkRes> genericResponse = new GenericResponse<>();
        try {
            AccessCodeBlkRes response = repository.bulkInsert(bulkUploadRequest);
            log.info("AreaCodeBlkRes {} ", response);
            if (response != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(response);
            } else {
                AccessCodeBlkRes resp = new AccessCodeBlkRes();
                resp.setTotalCount(String.valueOf(bulkUploadRequest.getBulkList().size()));
                resp.setBatchId(RefGenerator.getRefNo(5));
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                genericResponse.setOutputPayload(resp);
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param queryParam3
     * @param queryValue3
     * @param queryParam4
     * @param queryValue4
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<AccessCodeModel>> filter(String queryParam1, String queryValue1,
                                                         String queryParam2, String queryValue2,
                                                         String queryParam3, String queryValue3,
                                                         String queryParam4, String queryValue4,
                                                         String rowNumber) throws Exception {
        GenericResponse<List<AccessCodeModel>> genericResponse = new GenericResponse<>();
        try {
            List<AccessCodeModel> accessCodeModels = repository.filter(queryParam1, queryValue1, queryParam2,
                    queryValue2, queryParam3, queryValue3, queryParam4, queryValue4, rowNumber);
            log.info("Filtering: Result set from repository {} ====> ", accessCodeModels);

            if (accessCodeModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(accessCodeModels);
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

