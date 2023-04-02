package com.molcom.nms.number.configurations.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.configurations.dto.FrequencyCoverageArea;
import com.molcom.nms.number.configurations.repository.IFreqCoverageAreaRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FreqCoverageAreaService implements IFreqCoverageAreaService {
    @Autowired
    private IFreqCoverageAreaRepo repository;

    /**
     * Service implementation to save frequency coverage area
     *
     * @param frequencyCoverageArea
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<FrequencyCoverageArea> saveFreqCoverageArea(FrequencyCoverageArea frequencyCoverageArea) throws Exception {
        GenericResponse<FrequencyCoverageArea> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveFrequencyCoverageArea(frequencyCoverageArea);
            log.info("AutoFeeResponse code {} ", responseCode);
            if (responseCode == 1) {
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

    /**
     * Service implementation to delete frequency coverage area
     *
     * @param frequencyCoverageId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<FrequencyCoverageArea> deleteFreqCoverageArea(String frequencyCoverageId) throws Exception {
        GenericResponse<FrequencyCoverageArea> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteFrequencyCoverageArea(frequencyCoverageId);
            log.info("AutoFeeResponse code ", responseCode);
            if (responseCode == 1) {
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

    /**
     * Service implementation to get frequency coverage areas
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<FrequencyCoverageArea>> getFreqCoverageArea(String applicationId) throws Exception {
        GenericResponse<List<FrequencyCoverageArea>> genericResponse = new GenericResponse<>();
        try {
            List<FrequencyCoverageArea> frequencyCoverageAreaList = repository.getFrequencyCoverageArea(applicationId);
            log.info("Result set from repository {} ====> ", frequencyCoverageAreaList);

            if (frequencyCoverageAreaList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(frequencyCoverageAreaList);
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
}
