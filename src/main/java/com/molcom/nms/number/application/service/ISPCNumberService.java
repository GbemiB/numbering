package com.molcom.nms.number.application.service;

import com.molcom.nms.companyrep.dto.CompRepModel;
import com.molcom.nms.companyrep.repository.ICompRepRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.application.assembler.ISPCAssembler;
import com.molcom.nms.number.application.dto.ISPCNumberModel;
import com.molcom.nms.number.application.dto.ISPCNumberObject;
import com.molcom.nms.number.application.dto.ISPCNumberResponse;
import com.molcom.nms.number.application.repository.IISPCNumberRepository;
import com.molcom.nms.number.configurations.dto.ConnectedTelcoCompany;
import com.molcom.nms.number.configurations.dto.ISPCDetailModel;
import com.molcom.nms.number.configurations.dto.SupportingDocument;
import com.molcom.nms.number.configurations.repository.IConnectTelcoCompRepo;
import com.molcom.nms.number.configurations.repository.IIspcDetailRespository;
import com.molcom.nms.number.configurations.repository.ISupportingDocumentRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class ISPCNumberService implements IISPCNumberService {

    @Autowired
    private IISPCNumberRepository repository;
    @Autowired
    private IConnectTelcoCompRepo telcoCompRepository;
    @Autowired
    private IIspcDetailRespository ispcDetailRespository;
    @Autowired
    private ISupportingDocumentRepo supportingDocuRepo;
    @Autowired
    private ICompRepRepository compRepRepository;
    @Autowired
    private ISPCAssembler ispcAssembler;

    /**
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<ISPCNumberModel> saveISPCNumberFistStep(ISPCNumberModel model) throws Exception {
        GenericResponse<ISPCNumberModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveISPCFistStep(model);
            log.info("AutoFeeResponse code {} ", responseCode);
            if (responseCode == 1 || responseCode == 2) {
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
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<ISPCNumberModel> saveISPCNumberSecondStep(ISPCNumberModel model) throws Exception {
        GenericResponse<ISPCNumberModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveISPCSecondStep(model);
            log.info("AutoFeeResponse code {} ", responseCode);
            if (responseCode == 1 || responseCode == 2) {
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
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<ISPCNumberModel> saveISPCNumberThirdStep(ISPCNumberModel model) throws Exception {
        GenericResponse<ISPCNumberModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveISPCThirdStep(model);
            log.info("AutoFeeResponse code {} ", responseCode);
            if (responseCode == 1 || responseCode == 2) {
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
     * @param currentStep
     * @param applicationId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<ISPCNumberModel> saveISPCNumberDocStep(String currentStep, String applicationId) throws Exception {
        GenericResponse<ISPCNumberModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveISPCDocStep(currentStep, applicationId);
            log.info("AutoFeeResponse code {} ", responseCode);
            if (responseCode == 1 || responseCode == 2) {
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
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<ISPCNumberModel> deleteApplication(String applicationId) throws SQLException {
        GenericResponse<ISPCNumberModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteApplication(applicationId);
            log.info("AutoFeeResponse code ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!! {} ", exception.getMessage());
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
    public GenericResponse<List<ISPCNumberModel>> getFistStep(String applicationId) throws SQLException {
        GenericResponse<List<ISPCNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<ISPCNumberModel> ispcNumberModels = repository.getIspcNoFistStep(applicationId);
            log.info("Result set from repository {} ====> ", ispcNumberModels);

            if (ispcNumberModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(ispcNumberModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!! {} ", exception.getMessage());
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
    public GenericResponse<List<ISPCNumberModel>> getSecondStep(String applicationId) throws SQLException {
        GenericResponse<List<ISPCNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<ISPCNumberModel> ispcNumberModels = repository.getIspcNoSecondStep(applicationId);
            log.info("Result set from repository {} ====> ", ispcNumberModels);

            if (ispcNumberModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(ispcNumberModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!! {} ", exception.getMessage());
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
    public GenericResponse<List<ISPCNumberModel>> getFourthStep(String applicationId) throws SQLException {
        GenericResponse<List<ISPCNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<ISPCNumberModel> ispcNumberModels = repository.getIspcNoFourthStep(applicationId);
            log.info("Result set from repository {} ====> ", ispcNumberModels);

            if (ispcNumberModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(ispcNumberModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * @param ApplicationId
     * @param ApplicationType
     * @param ApplicationStatus
     * @param StartDate
     * @param EndDate
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<ISPCNumberModel>> filterISPCNumber(String companyName, String ApplicationId, String ApplicationType, String ApplicationStatus, String StartDate, String EndDate, String rowNumber) throws Exception {
        GenericResponse<List<ISPCNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<ISPCNumberModel> ispcNumberModels = repository.filterISPC(companyName, ApplicationId, ApplicationType,
                    ApplicationStatus, StartDate,
                    EndDate,
                    rowNumber);
            log.info("Result set from repository {} ====> ", ispcNumberModels);

            if (ispcNumberModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(ispcNumberModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param CompanyName
     * @param ApplicationId
     * @param ApplicationType
     * @param ApplicationStatus
     * @param StartDate
     * @param EndDate
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<ISPCNumberModel>> filterAdminISPCNumber(String CompanyName, String ApplicationId, String ApplicationType, String ApplicationStatus, String StartDate, String EndDate, String rowNumber) throws Exception {
        GenericResponse<List<ISPCNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<ISPCNumberModel> ispcNumberModels = repository.filterAdminISPC(CompanyName, ApplicationId, ApplicationType,
                    ApplicationStatus, StartDate,
                    EndDate,
                    rowNumber);
            log.info("Result set from repository {} ====> ", ispcNumberModels);

            if (ispcNumberModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(ispcNumberModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * @param applicationId
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public ISPCNumberResponse getISPCNumberById(String applicationId, String userId) throws Exception {
        ISPCNumberResponse ispcNumberResponse = new ISPCNumberResponse();
        try {
            // Call number setup for list of inputted configs
            List<ConnectedTelcoCompany> telco = telcoCompRepository.getConnectedTelcoCompanies(applicationId);
            List<SupportingDocument> documents = supportingDocuRepo.getSupportingRequiredDocument(applicationId);
            List<CompRepModel> representatives = compRepRepository.findByUserId(userId);
            List<ISPCDetailModel> ispcDetails = ispcDetailRespository.getIspcDetail(applicationId);
            ISPCNumberModel ispcNumberModel = repository.getISPCByApplicationId(applicationId);

            // Call short code assembler to assemble object
            ISPCNumberObject ispcNumberObject = ispcAssembler.ispcNumberObjectBuilder(ispcNumberModel);

            // Final get short code by id response building
            ispcNumberResponse.setIspcObject(ispcNumberObject);
            ispcNumberResponse.setConnectedTelcoCompanyList(telco);
            ispcNumberResponse.setSupportingDocumentList(documents);
            ispcNumberResponse.setCompRepModelList(representatives);
            ispcNumberResponse.setIspcDetailModels(ispcDetails);

        } catch (Exception exception) {
            log.info("Exception occurred while getting ISPC by application Id");
        }

        return ispcNumberResponse;
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<ISPCNumberModel>> getAll() throws Exception {
        GenericResponse<List<ISPCNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<ISPCNumberModel> ispcNumberModels = repository.getAll();
            log.info("Result set from repository {} ====> ", ispcNumberModels);

            if (ispcNumberModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(ispcNumberModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

}

