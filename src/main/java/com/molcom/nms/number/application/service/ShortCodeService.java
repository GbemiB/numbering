package com.molcom.nms.number.application.service;

import com.molcom.nms.companyrep.dto.CompRepModel;
import com.molcom.nms.companyrep.repository.ICompRepRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.application.assembler.ShortCodeAssembler;
import com.molcom.nms.number.application.dto.ShortCodeModel;
import com.molcom.nms.number.application.dto.ShortCodeObject;
import com.molcom.nms.number.application.dto.ShortCodeResponse;
import com.molcom.nms.number.application.repository.IShortCodeRepository;
import com.molcom.nms.number.configurations.dto.ConnectedTelcoCompany;
import com.molcom.nms.number.configurations.dto.EquipmentDetailModel;
import com.molcom.nms.number.configurations.dto.SupportingDocument;
import com.molcom.nms.number.configurations.repository.IConnectTelcoCompRepo;
import com.molcom.nms.number.configurations.repository.IEquipmentDetailsRepository;
import com.molcom.nms.number.configurations.repository.ISupportingDocumentRepo;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
import com.molcom.nms.number.selectedNumber.repository.ISelectedNumbersRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
public class ShortCodeService implements IShortCodeService {
    @Autowired
    private IShortCodeRepository repository;
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
    private ShortCodeAssembler shortCodeAssembler;

    /**
     * Service implementation to save short code application first step
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<ShortCodeModel> saveShortCodeAppFistStep(ShortCodeModel model) throws Exception {
        GenericResponse<ShortCodeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveShortCodeAppFistStep(model);
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
     * Service implementation to save short code application second step
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<ShortCodeModel> saveShortCodeAppSecondStep(ShortCodeModel model) throws Exception {
        GenericResponse<ShortCodeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveShortCodeAppSecondStep(model);
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
     * Service implementation to save short code application third step
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<ShortCodeModel> saveShortCodeAppThirdStep(ShortCodeModel model) throws Exception {
        GenericResponse<ShortCodeModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveShortCodeAppThirdStep(model);
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
    public GenericResponse<ShortCodeModel> saveShortCodeDocStep(String currentStep, String applicationId) throws Exception {
        GenericResponse<ShortCodeModel> genericResponse = new GenericResponse<>();
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
    public GenericResponse<ShortCodeModel> deleteApplication(String applicationId) throws SQLException {
        GenericResponse<ShortCodeModel> genericResponse = new GenericResponse<>();
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
    public GenericResponse<List<ShortCodeModel>> getFistStep(String applicationId) throws SQLException {
        GenericResponse<List<ShortCodeModel>> genericResponse = new GenericResponse<>();
        try {
            List<ShortCodeModel> models = repository.getShortNoFistStep(applicationId);
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
    public GenericResponse<List<ShortCodeModel>> getSecondStep(String applicationId) throws SQLException {
        GenericResponse<List<ShortCodeModel>> genericResponse = new GenericResponse<>();
        try {
            List<ShortCodeModel> models = repository.getShortNoSecondStep(applicationId);
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
    public GenericResponse<List<ShortCodeModel>> getFourthStep(String applicationId) throws SQLException {
        GenericResponse<List<ShortCodeModel>> genericResponse = new GenericResponse<>();
        try {
            List<ShortCodeModel> models = repository.getShortNoFourthStep(applicationId);
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
     * Service implementation to filter short codes
     *
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<ShortCodeModel>> filterShortCodes(String companyName, String ApplicationId, String ShortCodeCategory, String ShortCodeService, String ApplicationType, String ApplicationStatus, String ApplicationPaymentStatus, String AllocationPaymentStatus, String StartDate, String EndDate, String rowNumber) throws Exception {
        GenericResponse<List<ShortCodeModel>> genericResponse = new GenericResponse<>();
        try {
            List<ShortCodeModel> shortCodeModelList = repository.filterShortCodes(companyName, ApplicationId,
                    ShortCodeCategory,
                    ShortCodeService,
                    ApplicationType,
                    ApplicationStatus,
                    ApplicationPaymentStatus,
                    AllocationPaymentStatus,
                    StartDate, EndDate,
                    rowNumber);
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
     * @param ShortCodeCategory
     * @param ShortCodeService
     * @param ApplicationType
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
    public GenericResponse<List<ShortCodeModel>> filterAdminShortCodes(String CompanyName, String ApplicationId, String ShortCodeCategory, String ShortCodeService, String ApplicationType, String ApplicationStatus, String ApplicationPaymentStatus, String AllocationPaymentStatus, String StartDate, String EndDate, String rowNumber) throws Exception {
        GenericResponse<List<ShortCodeModel>> genericResponse = new GenericResponse<>();
        try {
            List<ShortCodeModel> shortCodeModelList = repository.filterAdminShortCodes(CompanyName, ApplicationId,
                    ShortCodeCategory,
                    ShortCodeService,
                    ApplicationType,
                    ApplicationStatus,
                    ApplicationPaymentStatus,
                    AllocationPaymentStatus,
                    StartDate, EndDate,
                    rowNumber);
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
     * Service implementation to get short code by application id
     * Call connected telecommunication repository to get all telco configured with the application
     * Call equipment details repository to get all equipment details with the application
     * Call selected numbers' repository to get all selected numbers configured with the application
     * Call supporting document repository to get all supporting documents configured with the application
     * Call company representative repository to get all company representative configured with the user
     * Call short code repository to get all short code by application id
     *
     * @param applicationId
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public ShortCodeResponse getShortCodeById(String applicationId, String userId) throws Exception {
        ShortCodeResponse shortCodeResponse = new ShortCodeResponse();
        try {
            // Call number setup for list of inputted configs
            List<ConnectedTelcoCompany> telco = telcoCompRepository.getConnectedTelcoCompanies(applicationId);
            List<EquipmentDetailModel> equipment = equipmentRepository.getEquipmentDetail(applicationId);
            List<SelectedNumberModel> selectedNos = selectedNoRepository.getSelectedNumber(applicationId);
            List<SupportingDocument> documents = supportingDocuRepo.getSupportingRequiredDocument(applicationId);
            List<CompRepModel> representatives = compRepRepository.findByUserId(userId);
            ShortCodeModel shortCodeModel = repository.getShortCodeByApplId(applicationId);

            // Call short code assembler to assemble object
            ShortCodeObject shortCodeObject = shortCodeAssembler.shortCodeRespBuilder(shortCodeModel);

            // Final get short code by id response building
            shortCodeResponse.setShortCodeObject(shortCodeObject);
            shortCodeResponse.setConnectedTelcoCompanyList(telco);
            shortCodeResponse.setEquipmentDetailModelList(equipment);
            shortCodeResponse.setSelectedNumberModelList(selectedNos);
            shortCodeResponse.setSupportingDocumentList(documents);
            shortCodeResponse.setCompRepModelList(representatives);

        } catch (Exception exception) {
            log.info("Exception occurred while getting short codes by application Id");
        }

        return shortCodeResponse;
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<ShortCodeModel>> getAll() throws Exception {
        GenericResponse<List<ShortCodeModel>> genericResponse = new GenericResponse<>();
        try {
            List<ShortCodeModel> shortCodeModelList = repository.getAll();
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
