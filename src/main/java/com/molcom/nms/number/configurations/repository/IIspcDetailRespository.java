package com.molcom.nms.number.configurations.repository;

import com.molcom.nms.number.configurations.dto.ISPCDetailModel;

import java.sql.SQLException;
import java.util.List;

public interface IIspcDetailRespository {
    int saveIspcDetail(ISPCDetailModel ispcDetailModel) throws SQLException;

    int deleteIspcDetail(String ispcDetailId) throws SQLException;

    List<ISPCDetailModel> getIspcDetail(String applicationId) throws SQLException;
}
