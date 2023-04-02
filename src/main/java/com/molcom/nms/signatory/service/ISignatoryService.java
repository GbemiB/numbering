package com.molcom.nms.signatory.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.signatory.dto.SignatoryModel;

import java.sql.SQLException;
import java.util.List;

public interface ISignatoryService {
    GenericResponse<SignatoryModel> save(SignatoryModel model) throws SQLException;

    GenericResponse<SignatoryModel> saveSignatoryToApplication(SignatoryModel model) throws SQLException;

    GenericResponse<SignatoryModel> edit(SignatoryModel model, int signatoryId) throws SQLException;

    GenericResponse<SignatoryModel> saveActive(int signatoryId) throws SQLException;

    GenericResponse<SignatoryModel> removeActive(int signatoryId) throws SQLException;

    GenericResponse<SignatoryModel> deleteById(int signatoryId) throws SQLException;

    GenericResponse<SignatoryModel> findByID(int signatoryId) throws SQLException;

    GenericResponse<List<SignatoryModel>> getAll(String rowNumber) throws SQLException;

    GenericResponse<List<SignatoryModel>> findSavedSignatory(String applicationId) throws SQLException;

    GenericResponse<List<SignatoryModel>> filter(String signatoryName, String signatoryDesignation, String rowNumber) throws SQLException;
}
