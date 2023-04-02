package com.molcom.nms.number.configurations.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.number.configurations.dto.ConnectedTelcoCompany;

import java.util.List;

public interface IConnectdTelcoCompService {
    GenericResponse<ConnectedTelcoCompany> saveConnectedTelcoCompanies(ConnectedTelcoCompany connectedTelcoCompany) throws Exception;

    GenericResponse<ConnectedTelcoCompany> deleteConnectedTelcoCompanies(String InterconnectId) throws Exception;

    GenericResponse<List<ConnectedTelcoCompany>> getConnectedTelcoCompanies(String applicationId) throws Exception;
}
