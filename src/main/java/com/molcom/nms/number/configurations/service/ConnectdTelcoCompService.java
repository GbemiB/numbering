package com.molcom.nms.number.configurations.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.configurations.dto.ConnectedTelcoCompany;
import com.molcom.nms.number.configurations.repository.IConnectTelcoCompRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ConnectdTelcoCompService implements IConnectdTelcoCompService {

    @Autowired
    private IConnectTelcoCompRepo repository;

    /**
     * Service implementation to add connected telecommunication
     *
     * @param connectedTelcoCompany
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<ConnectedTelcoCompany> saveConnectedTelcoCompanies(ConnectedTelcoCompany connectedTelcoCompany) throws Exception {
        GenericResponse<ConnectedTelcoCompany> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveConnectedTelcoCompanies(connectedTelcoCompany);
            log.info("AutoFeeResponse code of connected Telco Company save ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while saving connected Telco Company {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to edit existing connected telecommunication
     *
     * @param InterconnectId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<ConnectedTelcoCompany> deleteConnectedTelcoCompanies(String InterconnectId) throws Exception {
        GenericResponse<ConnectedTelcoCompany> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteConnectedTelcoCompanies(InterconnectId);
            log.info("AutoFeeResponse code of connected Telco Company delete ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while deleting connected Telco Company {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to get connected telecommunication
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<ConnectedTelcoCompany>> getConnectedTelcoCompanies(String applicationId) throws Exception {
        GenericResponse<List<ConnectedTelcoCompany>> genericResponse = new GenericResponse<>();
        try {
            List<ConnectedTelcoCompany> connectedTelcoCompanies = repository.getConnectedTelcoCompanies(applicationId);
            log.info("Find by Application Id: Result set from repository {} ====> ", connectedTelcoCompanies);

            if (connectedTelcoCompanies != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(connectedTelcoCompanies);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred while finding company representative by compRepId{} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }
}
