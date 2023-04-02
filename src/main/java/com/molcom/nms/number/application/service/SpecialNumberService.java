package com.molcom.nms.number.application.service;

import com.molcom.nms.companyrep.dto.CompRepModel;
import com.molcom.nms.companyrep.repository.ICompRepRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.application.assembler.SpecialNumberAssembler;
import com.molcom.nms.number.application.dto.SpecialNumberModel;
import com.molcom.nms.number.application.dto.SpecialNumberObject;
import com.molcom.nms.number.application.dto.SpecialNumberResponse;
import com.molcom.nms.number.application.repository.ISpecialNumberRepository;
import com.molcom.nms.number.configurations.dto.ConnectedTelcoCompany;
import com.molcom.nms.number.configurations.dto.EquipmentDetailModel;
import com.molcom.nms.number.configurations.dto.FrequencyCoverageArea;
import com.molcom.nms.number.configurations.dto.SupportingDocument;
import com.molcom.nms.number.configurations.repository.IConnectTelcoCompRepo;
import com.molcom.nms.number.configurations.repository.IEquipmentDetailsRepository;
import com.molcom.nms.number.configurations.repository.IFreqCoverageAreaRepo;
import com.molcom.nms.number.configurations.repository.ISupportingDocumentRepo;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
import com.molcom.nms.number.selectedNumber.repository.ISelectedNumbersRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class SpecialNumberService implements ISpecialNumberService {
    @Autowired
    private ISpecialNumberRepository repository;
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
    private SpecialNumberAssembler specialNumberAssembler;

    /**
     * Service implementation to save first step in special number application
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<SpecialNumberModel> saveSpecialNoFistStep(SpecialNumberModel model) throws Exception {
        GenericResponse<SpecialNumberModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveSpecialNoAppFistStep(model);
            log.info("AutoFeeResponse code from special number save {} ", responseCode);
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
     * Service implementation to save second step in special number application
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<SpecialNumberModel> saveSpecialNoSecondStep(SpecialNumberModel model) throws Exception {
        GenericResponse<SpecialNumberModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveSpecialNoAppSecondStep(model);
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
     * Service implementation to save third step in special number application
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<SpecialNumberModel> saveSpecialNoThirdStep(SpecialNumberModel model) throws Exception {
        GenericResponse<SpecialNumberModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveSpecialNoAppThirdStep(model);
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
    public GenericResponse<SpecialNumberModel> saveSpecialDocStep(String currentStep, String applicationId) throws Exception {
        GenericResponse<SpecialNumberModel> genericResponse = new GenericResponse<>();
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
    public GenericResponse<SpecialNumberModel> deleteApplication(String applicationId) throws SQLException {
        GenericResponse<SpecialNumberModel> genericResponse = new GenericResponse<>();
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
    public GenericResponse<List<SpecialNumberModel>> getFistStep(String applicationId) throws SQLException {
        GenericResponse<List<SpecialNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<SpecialNumberModel> models = repository.getSpecialNoFistStep(applicationId);
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
    public GenericResponse<List<SpecialNumberModel>> getSecondStep(String applicationId) throws SQLException {
        GenericResponse<List<SpecialNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<SpecialNumberModel> models = repository.getSpecialNoSecondStep(applicationId);
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
    public GenericResponse<List<SpecialNumberModel>> getFourthStep(String applicationId) throws SQLException {
        GenericResponse<List<SpecialNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<SpecialNumberModel> models = repository.getSpecialNoFourthStep(applicationId);
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
     * Service implementation to filter special numbers
     *
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<SpecialNumberModel>> filterSpecialNo(String companyName,
                                                                     String ApplicationId,
                                                                     String SubType,
                                                                     String ApplicationType,
                                                                     String AccessCode,
                                                                     String ApplicationStatus,
                                                                     String ApplicationPaymentStatus,
                                                                     String AllocationPaymentStatus,
                                                                     String StartDate,
                                                                     String EndDate,
                                                                     String rowNumber) throws Exception {
        GenericResponse<List<SpecialNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<SpecialNumberModel> shortCodeModelList = repository.filterSpecialNo(companyName, ApplicationId, SubType,
                    ApplicationType, AccessCode, ApplicationStatus, ApplicationPaymentStatus, AllocationPaymentStatus,
                    StartDate, EndDate, rowNumber);
            log.info("Result set from repository {} ====> ", shortCodeModelList);

            if (shortCodeModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(shortCodeModelList);
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
     * @param SubType
     * @param ApplicationType
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
    public GenericResponse<List<SpecialNumberModel>> filterAdminSpecialNo(String CompanyName, String ApplicationId, String SubType, String ApplicationType, String AccessCode, String ApplicationStatus, String ApplicationPaymentStatus, String AllocationPaymentStatus, String StartDate, String EndDate, String rowNumber) throws Exception {
        GenericResponse<List<SpecialNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<SpecialNumberModel> shortCodeModelList = repository.filterAdminSpecialNo(CompanyName, ApplicationId, SubType,
                    ApplicationType, AccessCode, ApplicationStatus, ApplicationPaymentStatus, AllocationPaymentStatus,
                    StartDate, EndDate, rowNumber);
            log.info("Result set from repository {} ====> ", shortCodeModelList);

            if (shortCodeModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(shortCodeModelList);
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
     * Call short code repository to get all short code by application id
     *
     * @param applicationId
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public SpecialNumberResponse getSpecialNoById(String applicationId, String userId) throws Exception {
        SpecialNumberResponse specialNumberResponse = new SpecialNumberResponse();
        try {
            // Call number setup for list of inputted configs
            List<ConnectedTelcoCompany> telco = telcoCompRepository.getConnectedTelcoCompanies(applicationId);
            List<EquipmentDetailModel> equipment = equipmentRepository.getEquipmentDetail(applicationId);
            List<SelectedNumberModel> selectedNos = selectedNoRepository.getSelectedNumber(applicationId);
            List<SupportingDocument> documents = supportingDocuRepo.getSupportingRequiredDocument(applicationId);
            List<CompRepModel> representatives = compRepRepository.findByUserId(userId);
            List<FrequencyCoverageArea> freqCoverage = freqCoverageAreaRepo.getFrequencyCoverageArea(applicationId);
            SpecialNumberModel specialNumberModel = repository.getSpecialNoByApplicationId(applicationId);

            // Call special number assembler to assemble object
            SpecialNumberObject specialNumberObject = specialNumberAssembler.specialNumberBuilder(specialNumberModel);

            // Final get special number by id response building
            specialNumberResponse.setSpecialNumberObject(specialNumberObject);
            specialNumberResponse.setConnectedTelcoCompanyList(telco);
            specialNumberResponse.setEquipmentDetailModelList(equipment);
            specialNumberResponse.setSelectedNumberModelList(selectedNos);
            specialNumberResponse.setSupportingDocumentList(documents);
            specialNumberResponse.setCompRepModelList(representatives);
            specialNumberResponse.setFrequencyCoverageAreaList(freqCoverage);

        } catch (Exception exception) {
            log.info("Exception occurred while getting special number by application Id");
        }

        return specialNumberResponse;
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<SpecialNumberModel>> getAll() throws Exception {
        GenericResponse<List<SpecialNumberModel>> genericResponse = new GenericResponse<>();
        try {
            List<SpecialNumberModel> shortCodeModelList = repository.getAll();
            log.info("Result set from repository {} ====> ", shortCodeModelList);

            if (shortCodeModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(shortCodeModelList);
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
