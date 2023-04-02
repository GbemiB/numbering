package com.molcom.nms.number.configurations.repository;

import com.molcom.nms.number.configurations.dto.SupportingDocument;

import java.sql.SQLException;
import java.util.List;

public interface ISupportingDocumentRepo {
    int saveSupportingDocument(SupportingDocument supportingDocument) throws SQLException;

    int updateSupportingDocument(SupportingDocument supportingDocument) throws SQLException;

    int deleteSupportingDocumentById(String documentId) throws SQLException;

    int deleteSupportingDocumentByName(String documentName) throws SQLException;

    List<SupportingDocument> getSupportingRequiredDocument(String applicationId) throws SQLException;

    List<SupportingDocument> getSupportingAdditionalDocument(String applicationId) throws SQLException;

    List<SupportingDocument> getAllDocumentByApplication(String applicationId) throws SQLException;
}
