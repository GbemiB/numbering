package com.molcom.nms.number.configurations.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.number.configurations.dto.FrequencyCoverageArea;

import java.util.List;

public interface IFreqCoverageAreaService {
    GenericResponse<FrequencyCoverageArea> saveFreqCoverageArea(FrequencyCoverageArea frequencyCoverageArea) throws Exception;

    GenericResponse<FrequencyCoverageArea> deleteFreqCoverageArea(String frequencyCoverageId) throws Exception;

    GenericResponse<List<FrequencyCoverageArea>> getFreqCoverageArea(String applicationId) throws Exception;
}
