package com.molcom.nms.number.configurations.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.number.configurations.dto.SupportingDocument;

import java.sql.SQLException;
import java.util.List;

public interface ISupportingDocumentService {
    GenericResponse<SupportingDocument> saveSupportingDoc(SupportingDocument supportingDocument) throws Exception;

    GenericResponse<List<SupportingDocument>> multipleSaveSupportingDoc(List<SupportingDocument> supportingDocument) throws Exception;

    GenericResponse<SupportingDocument> updateSupportingDoc(SupportingDocument supportingDocument) throws Exception;

    GenericResponse<SupportingDocument> deleteSupportingDocById(String documentId) throws Exception;

    GenericResponse<SupportingDocument> deleteSupportingDocByName(String documentName) throws Exception;

    GenericResponse<List<SupportingDocument>> getRequiredSupportingDoc(String applicationId) throws Exception;

    GenericResponse<List<SupportingDocument>> getAdditionalSupportingDoc(String applicationId) throws Exception;

    GenericResponse<List<SupportingDocument>> getAllDocumentByApplication(String applicationId) throws SQLException;
}
