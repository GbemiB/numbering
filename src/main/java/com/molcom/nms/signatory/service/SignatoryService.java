package com.molcom.nms.signatory.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.signatory.dto.SignatoryModel;
import com.molcom.nms.signatory.repository.ISignatoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
public class SignatoryService implements ISignatoryService {

    @Autowired
    ISignatoryRepo repository;

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<SignatoryModel> save(SignatoryModel model) throws SQLException {
        GenericResponse<SignatoryModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.save(model);
            log.info("AutoFeeResponse code from save ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while saving new company representative {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<SignatoryModel> saveSignatoryToApplication(SignatoryModel model) throws SQLException {
        GenericResponse<SignatoryModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveSignatoryToApplication(model);
            log.info("AutoFeeResponse code of from save ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while saving new company representative {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param model
     * @param signatoryId
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<SignatoryModel> edit(SignatoryModel model, int signatoryId) throws SQLException {
        GenericResponse<SignatoryModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.edit(model, signatoryId);
            log.info("AutoFeeResponse code from edit ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while editing company representative {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param signatoryId
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<SignatoryModel> saveActive(int signatoryId) throws SQLException {
        GenericResponse<SignatoryModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            int count = repository.checkActiveCount();
            log.info("Checking if any signatory is active because only one active signatory is needed {} ", count);
            if (count >= 1) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage("There is already an active signatory");
            } else {
                responseCode = repository.setAsActive(signatoryId);
                log.info("AutoFeeResponse code {} ", responseCode);
                if (responseCode == 1) {
                    genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                } else {
                    genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
                }
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param signatoryId
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<SignatoryModel> removeActive(int signatoryId) throws SQLException {
        GenericResponse<SignatoryModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.removeAsActive(signatoryId);
            log.info("AutoFeeResponse code  ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred  {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param signatoryId
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<SignatoryModel> deleteById(int signatoryId) throws SQLException {
        GenericResponse<SignatoryModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteById(signatoryId);
            log.info("AutoFeeResponse code  ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param signatoryId
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<SignatoryModel> findByID(int signatoryId) throws SQLException {
        GenericResponse<SignatoryModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            SignatoryModel signatoryModel = repository.findByID(signatoryId);
            log.info("Result set from repository {} ====> ", signatoryModel);
            if (signatoryModel != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(signatoryModel);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred } ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<List<SignatoryModel>> getAll(String rowNumber) throws SQLException {
        GenericResponse<List<SignatoryModel>> genericResponse = new GenericResponse<>();
        try {
            List<SignatoryModel> signatory = repository.getAll(rowNumber);
            log.info("Result set from repository {} ====> ", signatory);

            if (signatory != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(signatory);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred while finding signatory  {} ", exception.getMessage());
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
    public GenericResponse<List<SignatoryModel>> findSavedSignatory(String applicationId) throws SQLException {
        GenericResponse<List<SignatoryModel>> genericResponse = new GenericResponse<>();
        try {
            List<SignatoryModel> signatory = repository.findSavedSignatory(applicationId);
            log.info("Result set from repository {} ====> ", signatory);

            if (signatory != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(signatory);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred while finding signatory  {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param signatoryName
     * @param signatoryDesignation
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<List<SignatoryModel>> filter(String signatoryName, String signatoryDesignation, String rowNumber) throws SQLException {
        GenericResponse<List<SignatoryModel>> genericResponse = new GenericResponse<>();
        try {
            List<SignatoryModel> signatoryModels = repository.filter(signatoryName, signatoryDesignation, rowNumber);
            log.info("Result set from repository {} ====> ", signatoryModels);

            if (signatoryModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(signatoryModels);
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
