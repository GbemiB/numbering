package com.molcom.nms.signatory.repository;

import com.molcom.nms.signatory.dto.SignatoryModel;

import java.sql.SQLException;
import java.util.List;

public interface ISignatoryRepo {
    int save(SignatoryModel model) throws SQLException;

    int saveSignatoryToApplication(SignatoryModel model) throws SQLException;

    int edit(SignatoryModel model, int signatoryId) throws SQLException;

    int checkActiveCount() throws SQLException;

    int setAsActive(int signatoryId) throws SQLException;

    int removeAsActive(int signatoryId) throws SQLException;

    int deleteById(int signatoryId) throws SQLException;

    SignatoryModel findByID(int signatoryId) throws SQLException;

    List<SignatoryModel> getAll(String rowNumber) throws SQLException;

    SignatoryModel getActiveSignatory() throws SQLException;

    List<SignatoryModel> findSavedSignatory(String applicationId) throws SQLException;

    List<SignatoryModel> filter(String signatoryName, String signatoryDesignation, String rowNumber) throws SQLException;
}
