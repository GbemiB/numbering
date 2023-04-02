package com.molcom.nms.additionofservice.service;

import com.molcom.nms.additionofservice.dto.AdditionOfServiceModel;
import com.molcom.nms.additionofservice.dto.AvailableForAdditionOfService;
import com.molcom.nms.additionofservice.repository.AdditionOfServiceRepo;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.application.dto.ShortCodeModel;
import com.molcom.nms.number.application.repository.ShortCodeRepository;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdditionOfService implements IAdditionOfService {

    @Autowired
    private AdditionOfServiceRepo repository;

    @Autowired
    private ShortCodeRepository shortCodeRepository;

    /**
     * Service implementation to save addition of service application
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<AdditionOfServiceModel> save(AdditionOfServiceModel model) throws Exception {
        GenericResponse<AdditionOfServiceModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            int count = repository.doesExist(model);
            if (count >= 1) {
                genericResponse.setResponseCode(ResponseStatus.ALREADY_EXIST.getCode());
                genericResponse.setResponseMessage("This addition Service already exist for this short code");
            } else {
                responseCode = repository.save(model);
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
            log.info("Exception occurred here ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;

    }

    /**
     * Service implementation to get allocated number per company name
     *
     * @param companyName
     * @return
     * @throws SQLException
     */
    @Override
    public GenericResponse<List<AvailableForAdditionOfService>> getAllocatedNos(String companyName) throws SQLException {
        GenericResponse<List<AvailableForAdditionOfService>> genericResponse = new GenericResponse<>();
        List<AvailableForAdditionOfService> resultSet = new ArrayList<>();
        try {
            // get allocated selected number
            List<SelectedNumberModel> serviceModels = repository.getAllocatedNos(companyName);
            log.info("Number due for addition of services {}", serviceModels);

            if (serviceModels != null) {
                // for each, get short code category from short code table
                serviceModels.forEach(serv -> {
                    try {
                        AvailableForAdditionOfService model = new AvailableForAdditionOfService();

                        String applicationId = serv.getApplicationId();
                        String number = serv.getSelectedNumberValue();
                        String purpose = serv.getPurpose();

                        ShortCodeModel shortCodeModel = shortCodeRepository.getShortCodeByApplId(applicationId);
                        String shortCodeCategory = (shortCodeModel != null ? shortCodeModel.getShortCodeCategory() : "");

                        model.setApplicationId(applicationId);
                        model.setPurpose(purpose);
                        model.setShortCode(number);
                        model.setShortCodeCategory(shortCodeCategory); // pass the short code category

                        resultSet.add(model);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(resultSet);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred!!!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to delete addition of service application per id
     *
     * @param additionOfServiceId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<AdditionOfServiceModel> deleteById(int additionOfServiceId) throws Exception {
        GenericResponse<AdditionOfServiceModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteById(additionOfServiceId);
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
     * Service implemenation to get all addition of service applications
     *
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<AdditionOfServiceModel>> getAll() throws Exception {
        GenericResponse<List<AdditionOfServiceModel>> genericResponse = new GenericResponse<>();
        try {
            List<AdditionOfServiceModel> serviceModels = repository.getAll();
            log.info("Result set from repository {} ====> ", serviceModels);
            if (serviceModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(serviceModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred!!!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    @Override
    public GenericResponse<List<AdditionOfServiceModel>> getAllByOrganisation(String organisation) throws Exception {
        GenericResponse<List<AdditionOfServiceModel>> genericResponse = new GenericResponse<>();
        try {
            List<AdditionOfServiceModel> serviceModels = repository.getAllByOrganisation(organisation);
            log.info("Result set from repository {} ====> ", serviceModels);
            if (serviceModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(serviceModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred!!!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to find addition of service per id
     *
     * @param additionOfServiceId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<AdditionOfServiceModel> findById(String additionOfServiceId) throws Exception {
        GenericResponse<AdditionOfServiceModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            AdditionOfServiceModel serviceModel = repository.findById(additionOfServiceId);
            log.info("Result set from repository {} ====> ", serviceModel);
            if (serviceModel != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(serviceModel);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred !!!!", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to filter addition of service by search parameters
     *
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param queryValue3
     * @param queryValue4
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<AdditionOfServiceModel>> filter(String queryParam1, String queryValue1, String queryParam2,
                                                                String queryValue2,
                                                                String queryValue3,
                                                                String queryValue4, String rowNumber) throws Exception {
        GenericResponse<List<AdditionOfServiceModel>> genericResponse = new GenericResponse<>();
        try {
            List<AdditionOfServiceModel> serviceModelList = repository.filter(queryParam1, queryValue1, queryParam2,
                    queryValue2,
                    queryValue3,
                    queryValue4, rowNumber);
            log.info("Result set from repository {} ====> ", serviceModelList);

            if (serviceModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(serviceModelList);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while filtering Addtion of Service {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    @Override
    public GenericResponse<List<AdditionOfServiceModel>> filterForRegularUser(String companyName, String queryParam1, String queryValue1, String queryParam2, String queryValue2, String queryValue3, String queryValue4, String rowNumber) throws Exception {
        GenericResponse<List<AdditionOfServiceModel>> genericResponse = new GenericResponse<>();
        try {
            List<AdditionOfServiceModel> serviceModelList = repository.filterForRegularUser(companyName, queryParam1, queryValue1, queryParam2,
                    queryValue2,
                    queryValue3,
                    queryValue4, rowNumber);
            log.info("Result set from repository {} ====> ", serviceModelList);

            if (serviceModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(serviceModelList);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while filtering Addtion of Service {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }
}
