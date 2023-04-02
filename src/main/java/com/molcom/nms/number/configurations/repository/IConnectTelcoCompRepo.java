package com.molcom.nms.number.configurations.repository;

import com.molcom.nms.number.configurations.dto.ConnectedTelcoCompany;

import java.sql.SQLException;
import java.util.List;

public interface IConnectTelcoCompRepo {
    int saveConnectedTelcoCompanies(ConnectedTelcoCompany connectedTelcoCompany) throws SQLException;

    int deleteConnectedTelcoCompanies(String interconnectId) throws SQLException;

    List<ConnectedTelcoCompany> getConnectedTelcoCompanies(String applicationId) throws SQLException;
}
