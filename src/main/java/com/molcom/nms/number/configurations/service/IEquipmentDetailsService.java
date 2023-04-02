package com.molcom.nms.number.configurations.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.number.configurations.dto.EquipmentDetailModel;

import java.util.List;

public interface IEquipmentDetailsService {
    GenericResponse<EquipmentDetailModel> saveEquipmentDetail(EquipmentDetailModel equipmentDetailModel) throws Exception;

    GenericResponse<EquipmentDetailModel> deleteEquipmentDetail(String equipmentId) throws Exception;

    GenericResponse<List<EquipmentDetailModel>> getEquipmentDetail(String applicationId) throws Exception;
}
