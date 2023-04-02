package com.molcom.nms.number.application.service;

import com.molcom.nms.companyrep.dto.CompRepModel;
import com.molcom.nms.companyrep.repository.ICompRepRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.application.assembler.StandardNumberAssembler;
import com.molcom.nms.number.application.dto.StandardNumberModel;
import com.molcom.nms.number.application.dto.StandardNumberObject;
import com.molcom.nms.number.application.dto.StandardNumberResponse;
import com.molcom.nms.number.application.repository.IStandardNumberRepository;
import com.molcom.nms.number.configurations.dto.*;
import com.molcom.nms.number.configurations.repository.*;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
import com.molcom.nms.number.selectedNumber.repository.ISelectedNumbersRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class StandardNumberService implements IStandardNumberService {
    @Autowired
    private IStandardNumberRepository repository;
    @Autowired
    private IConnectTelcoCompRepo telcoCompRepository;
    @Autowired
    private IEquipmentDetailsRepository equipmentRepository;
    @Autowired
    private ISelectedNumbersRepo selectedNoRepository;
    @Autowired
    private ISupportingDocumentRepo supportingDocuRepo;
    @Autowired
    private ICompRepRepository compRepRepository;
    @Autowired
    private IFreqCoverageAreaRepo freqCoverageAreaRepo;
    @Autowired
    private IDeviceRepository deviceRepository;
    @Autowired
    private StandardNumberAssembler standardNumberAssembler;

    /**
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<StandardNumberModel> saveStandardNoFistStep(StandardNumberModel model) throws Exception {
        GenericResponse<StandardNumberModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveStandardNoFistStep(model);
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
    public GenericResponse<StandardNumberModel> saveStandardNoSecondStep(StandardNumberModel model) throws Exception {
        GenericResponse<StandardNumberModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveStandardNoSecondStep(model);
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
    public GenericResponse<StandardNumberModel> saveStandardNoThirdStep(StandardNumberModel model) throws Exception {
        GenericResponse<StandardNumberModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveStandardNoThirdStep(model);
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
    public GenericResponse<StandardNumberModel> saveStandardDocStep(String currentStep, String applicationId) throws Exception {
        GenericResponse<StandardNumberModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveDocFourthStep(currentStep, applicationId);
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
    public GenericResponse<StandardNumberModel> deleteApplication(String applicationId) throws SQLException {
        GenericResponse<StandardNumberModel> genericResponse = new GenericResponse<>();
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
    public GenericResponse<List<StandardNumberModel>> getFistStep(String applicationId) throws SQLException {
        GenericResponse<List<StandardNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<StandardNumberModel> models = repository.getStandardNoFistStep(applicationId);
            log.info("Result set from repository {} ====> ", models);

            if (models != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(models);
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
    public GenericResponse<List<StandardNumberModel>> getSecondStep(String applicationId) throws SQLException {
        GenericResponse<List<StandardNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<StandardNumberModel> models = repository.getStandardNoSecondStep(applicationId);
            log.info("Result set from repository {} ====> ", models);

            if (models != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(models);
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
    public GenericResponse<List<StandardNumberModel>> getFourthStep(String applicationId) throws SQLException {
        GenericResponse<List<StandardNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<StandardNumberModel> models = repository.getStandardNoFourthStep(applicationId);
            log.info("Result set from repository {} ====> ", models);

            if (models != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(models);
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
     * Service implementation to filter standard numbers
     *
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<StandardNumberModel>> filterStandardNo(String companyName, String ApplicationId, String SubType, String ApplicationType, String CoverageArea, String AreaCode, String AccessCode, String ApplicationStatus, String ApplicationPaymentStatus, String AllocationPaymentStatus, String StartDate, String EndDate, String rowNumber) throws Exception {
        GenericResponse<List<StandardNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<StandardNumberModel> standardNumberModels = repository.filterStandardNo(companyName, ApplicationId, SubType, ApplicationType, CoverageArea, AreaCode
                    , AccessCode, ApplicationStatus, ApplicationPaymentStatus, AllocationPaymentStatus, StartDate
                    , EndDate, rowNumber);
            log.info("Result set from repository {} ====> ", standardNumberModels);

            if (standardNumberModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(standardNumberModels);
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
     * @param CompanyName
     * @param SubType
     * @param ApplicationType
     * @param CoverageArea
     * @param AreaCode
     * @param AccessCode
     * @param ApplicationStatus
     * @param ApplicationPaymentStatus
     * @param AllocationPaymentStatus
     * @param StartDate
     * @param EndDate
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<StandardNumberModel>> filterAdminStandardNo(String ApplicationId, String CompanyName, String SubType, String ApplicationType, String CoverageArea, String AreaCode, String AccessCode, String ApplicationStatus, String ApplicationPaymentStatus, String AllocationPaymentStatus, String StartDate, String EndDate, String rowNumber) throws Exception {
        GenericResponse<List<StandardNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<StandardNumberModel> standardNumberModels = repository.filterAminStandardNo(CompanyName, ApplicationId, SubType, ApplicationType, CoverageArea, AreaCode
                    , AccessCode, ApplicationStatus, ApplicationPaymentStatus, AllocationPaymentStatus, StartDate
                    , EndDate, rowNumber);
            log.info("Result set from repository {} ====> ", standardNumberModels);

            if (standardNumberModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(standardNumberModels);
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
     * Service implementation to get special code by application id
     * Call connected telecommunication repository to get all telco configured with the application
     * Call equipment details repository to get all equipment details with the application
     * Call selected numbers' repository to get all selected numbers configured with the application
     * Call supporting document repository to get all supporting documents configured with the application
     * Call company representative repository to get all company representative configured with the user
     * Call frequent coverage ara repository to get all configured frequent coverages
     * Call device repository to get all confugured devices
     *
     * @param applicationId
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public StandardNumberResponse getStandardNoById(String applicationId, String userId) throws Exception {
        StandardNumberResponse standardNumberResponse = new StandardNumberResponse();
        try {
            // Call number setup for list of inputted configs
            List<ConnectedTelcoCompany> telco = telcoCompRepository.getConnectedTelcoCompanies(applicationId);
            List<EquipmentDetailModel> equipment = equipmentRepository.getEquipmentDetail(applicationId);
            List<SelectedNumberModel> selectedNos = selectedNoRepository.getSelectedNumber(applicationId);
            List<SupportingDocument> documents = supportingDocuRepo.getSupportingRequiredDocument(applicationId);
            List<CompRepModel> representatives = compRepRepository.findByUserId(userId);
            List<FrequencyCoverageArea> freqCoverage = freqCoverageAreaRepo.getFrequencyCoverageArea(applicationId);
            List<DeviceModel> device = deviceRepository.getDevice(applicationId);
            StandardNumberModel standardNumberModel = repository.getStandardNoByApplicationId(applicationId);

            // Call standard number assembler to assemble object
            StandardNumberObject standardNumberObject = standardNumberAssembler.standardNumberBuilder(
                    standardNumberModel);

            // Final get standard number by id response building
            standardNumberResponse.setStandardNumberObject(standardNumberObject);
            standardNumberResponse.setConnectedTelcoCompanyList(telco);
            standardNumberResponse.setEquipmentDetailModelList(equipment);
            standardNumberResponse.setSelectedNumberModelList(selectedNos);
            standardNumberResponse.setSupportingDocumentList(documents);
            standardNumberResponse.setCompRepModelList(representatives);
            standardNumberResponse.setFrequencyCoverageAreaList(freqCoverage);
            standardNumberResponse.setDeviceModelList(device);

        } catch (Exception exception) {
            log.info("Exception occurred while getting standard number by application Id");
        }

        return standardNumberResponse;
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<StandardNumberModel>> getAll() throws Exception {
        GenericResponse<List<StandardNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<StandardNumberModel> standardNumberModels = repository.getAll();
            log.info("Result set from repository {} ====> ", standardNumberModels);

            if (standardNumberModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(standardNumberModels);
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

