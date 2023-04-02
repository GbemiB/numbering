package com.molcom.nms.number.configurations.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.configurations.dto.SupportingDocument;
import com.molcom.nms.number.configurations.repository.ISupportingDocumentRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
public class SupportingDocumentService implements ISupportingDocumentService {
    @Autowired
    private ISupportingDocumentRepo repository;

    /**
     * Service implementation to save selected number
     *
     * @param supportingDocument
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<SupportingDocument> saveSupportingDoc(SupportingDocument supportingDocument) throws Exception {
        GenericResponse<SupportingDocument> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveSupportingDocument(supportingDocument);
            log.info("AutoFeeResponse code {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param list
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<SupportingDocument>> multipleSaveSupportingDoc(List<SupportingDocument> list) throws Exception {
        GenericResponse<List<SupportingDocument>> genericResponse = new GenericResponse<>();
        try {
            SupportingDocument model = new SupportingDocument();
            list.forEach(doc -> {
                model.setDocumentId(doc.getDocumentId());
                model.setDocumentName(doc.getDocumentName());
                model.setDocumentBase64String(doc.getDocumentBase64String());
                model.setCreatedDate(doc.getCreatedDate());
                model.setDocumentFileName(doc.getDocumentFileName());
                model.setIsRequired(doc.getIsRequired());
                model.setApplicationId(doc.getApplicationId());
                try {
                    int resCode = repository.saveSupportingDocument(model);
                    log.info("Save response {}", resCode);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
            genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());

        } catch (Exception exception) {
            log.info("Exception occurred while saving supporting number  {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
        }
        return genericResponse;
    }


    /**
     * @param supportingDocument
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<SupportingDocument> updateSupportingDoc(SupportingDocument supportingDocument) throws Exception {
        GenericResponse<SupportingDocument> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.updateSupportingDocument(supportingDocument);
            log.info("AutoFeeResponse code {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to delete supporting document
     *
     * @param documentId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<SupportingDocument> deleteSupportingDocById(String documentId) throws Exception {
        GenericResponse<SupportingDocument> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteSupportingDocumentById(documentId);
            log.info("AutoFeeResponse code {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * @param documentName
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<SupportingDocument> deleteSupportingDocByName(String documentName) throws Exception {
        GenericResponse<SupportingDocument> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteSupportingDocumentByName(documentName);
            log.info("AutoFeeResponse code {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to get supporting documents by application id
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<SupportingDocument>> getRequiredSupportingDoc(String applicationId) throws Exception {
        GenericResponse<List<SupportingDocument>> genericResponse = new GenericResponse<>();
        try {
            List<SupportingDocument> supportingDocuments = repository.getSupportingRequiredDocument(applicationId);
            log.info("Result set from repository {} ====> ", supportingDocuments);

            if (supportingDocuments != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(supportingDocuments);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param applicationId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<SupportingDocument>> getAdditionalSupportingDoc(String applicationId) throws Exception {
        GenericResponse<List<SupportingDocument>> genericResponse = new GenericResponse<>();
        try {
            List<SupportingDocument> supportingDocuments = repository.getSupportingAdditionalDocument(applicationId);
            log.info("Result set from repository {} ====> ", supportingDocuments);

            if (supportingDocuments != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(supportingDocuments);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<List<SupportingDocument>> getAllDocumentByApplication(String applicationId) throws SQLException {
        GenericResponse<List<SupportingDocument>> genericResponse = new GenericResponse<>();
        try {
            List<SupportingDocument> supportingDocuments = repository.getAllDocumentByApplication(applicationId);
            log.info("Result set from repository {} ====> ", supportingDocuments);

            if (supportingDocuments != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(supportingDocuments);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }
}

