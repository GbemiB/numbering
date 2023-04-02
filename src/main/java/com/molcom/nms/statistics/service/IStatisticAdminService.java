package com.molcom.nms.statistics.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.statistics.dto.AdminUserStatistics;

public interface IStatisticAdminService {
    GenericResponse<AdminUserStatistics> getAdminStatistics();
}
