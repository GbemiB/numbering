package com.molcom.nms.number.configurations.repository;

import com.molcom.nms.number.configurations.dto.FrequencyCoverageArea;

import java.sql.SQLException;
import java.util.List;

public interface IFreqCoverageAreaRepo {
    int saveFrequencyCoverageArea(FrequencyCoverageArea frequencyCoverageArea) throws SQLException;

    int deleteFrequencyCoverageArea(String frequencyCoverageId) throws SQLException;

    List<FrequencyCoverageArea> getFrequencyCoverageArea(String applicationId) throws SQLException;
}
