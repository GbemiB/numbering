package com.molcom.nms.statistics.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.statistics.dto.RegularUserStatistics;

public interface IStatisticRegularService {
    GenericResponse<RegularUserStatistics> getRegularStatistics();

    GenericResponse<RegularUserStatistics> getRegularStatistics(String companyName);
}
