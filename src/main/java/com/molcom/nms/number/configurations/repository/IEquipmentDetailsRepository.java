package com.molcom.nms.number.configurations.repository;

import com.molcom.nms.number.configurations.dto.EquipmentDetailModel;

import java.sql.SQLException;
import java.util.List;

public interface IEquipmentDetailsRepository {
    int saveEquipmentDetail(EquipmentDetailModel equipmentDetailModel) throws SQLException;

    int deleteEquipmentDetail(String equipmentId) throws SQLException;

    List<EquipmentDetailModel> getEquipmentDetail(String applicationId) throws SQLException;
}
