package com.molcom.nms.coveragearea.service;

import com.molcom.nms.coveragearea.dto.BulkUploadRequest;
import com.molcom.nms.coveragearea.dto.BulkUploadResponse;
import com.molcom.nms.coveragearea.dto.CoverageAreaModel;
import com.molcom.nms.coveragearea.repository.CoverageAreaRepo;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CoverageAreaService implements ICoverageAreaService {

    @Autowired
    private CoverageAreaRepo repository;

    /**
     * Service implementation to save coverage area
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<CoverageAreaModel> save(CoverageAreaModel model) throws Exception {
        GenericResponse<CoverageAreaModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            int isExist = repository.accessCodeCount(model.getCoverageName());
            if (isExist >= 1) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage("Coverage area already exist");
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

    @Override
    public GenericResponse<BulkUploadResponse> bulkUpload(BulkUploadRequest bulkUploadRequest) throws Exception {
        GenericResponse<BulkUploadResponse> genericResponse = new GenericResponse<>();
        try {
            BulkUploadResponse response = repository.bulkInsert(bulkUploadRequest);
            log.info("AreaCodeBlkRes {} ", response);
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

    /**
     * Service implementation to edit coverage area
     *
     * @param model
     * @param coverageId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<CoverageAreaModel> edit(CoverageAreaModel model, int coverageId) throws Exception {
        GenericResponse<CoverageAreaModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.edit(model, coverageId);
            log.info("AutoFeeResponse code {} ", responseCode);
            if (responseCode >= 1) {
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
     * Service implementation to delete by id coverage area
     *
     * @param coverageId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<CoverageAreaModel> deleteById(int coverageId) throws Exception {
        GenericResponse<CoverageAreaModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteById(coverageId);
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
     * Service implementation to get all coverage areas
     *
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<CoverageAreaModel>> getAll() throws Exception {
        GenericResponse<List<CoverageAreaModel>> genericResponse = new GenericResponse<>();
        try {
            List<CoverageAreaModel> coverageAreaModels = repository.getAll();
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

    @Override
    public GenericResponse<List<CoverageAreaModel>> getCoverageType(String coverageType) throws Exception {
        GenericResponse<List<CoverageAreaModel>> genericResponse = new GenericResponse<>();
        try {
            List<CoverageAreaModel> coverageAreaModels = repository.getByCoverageType(coverageType);
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
            log.info("Exception occurred !!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * Service implementation to find coverage area bu id
     *
     * @param coverageId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<CoverageAreaModel> findById(int coverageId) throws Exception {
        GenericResponse<CoverageAreaModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            CoverageAreaModel coverageAreaModel = repository.findById(coverageId);
            log.info("Result set from repository {} ====> ", coverageAreaModel);
            if (coverageAreaModel != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(coverageAreaModel);
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
     * Service implementation to filterForRegularUser coverage areas
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
    public GenericResponse<List<CoverageAreaModel>> filter(String queryParam1, String queryValue1, String queryParam2, String queryValue2, String rowNumber) throws Exception {
        GenericResponse<List<CoverageAreaModel>> genericResponse = new GenericResponse<>();
        log.info("Query parameter for filtering {} ", queryParam2);
        try {
            List<CoverageAreaModel> coverageAreaModels = repository.filter(queryParam1, queryValue1, queryParam2, queryValue2, rowNumber);
            log.info("Filtering: Result set from repository {} ====> ", coverageAreaModels);

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

}
