package com.molcom.nms.numberCreation.availableNumbers.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.numberCreation.availableNumbers.dto.GetAvailableNumber;
import com.molcom.nms.numberCreation.ispc.dto.CreateIspcNoModel;
import com.molcom.nms.numberCreation.ispc.repository.CreateIspcRepository;
import com.molcom.nms.numberCreation.shortcode.dto.CreateShortCodeNoModel;
import com.molcom.nms.numberCreation.shortcode.repository.CreateShortCodeRepository;
import com.molcom.nms.numberCreation.special.dto.CreateSpecialNoModel;
import com.molcom.nms.numberCreation.special.repository.CreateSpecialRepository;
import com.molcom.nms.numberCreation.standard.dto.CreateStandardNoModel;
import com.molcom.nms.numberCreation.standard.repository.CreateStandardNoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AvailableNumbersService {
    @Autowired
    private CreateSpecialRepository specialRepository;

    @Autowired
    private CreateStandardNoRepository standardNoRepository;

    @Autowired
    private CreateShortCodeRepository shortCodeRepository;

    @Autowired
    private CreateIspcRepository ispcRepository;

    public GenericResponse<List<?>> getAvailableNumbers(GetAvailableNumber request) {
        log.info("Get available numbers model {}", request);
        GenericResponse<List<?>> genericResponse = new GenericResponse<>();

        String accessCode = request.getAccessCode();
        String serviceName = request.getShortCodeServiceName();
        String numberType = request.getNumberSubType().toUpperCase();

        log.info("NumberSubtype {}", numberType);
        switch (numberType) {
            case "NATIONAL":
                try {
                    // Get available number block for national number for specific access code
                    List<CreateStandardNoModel> modelList = standardNoRepository.getAvailableNationalNo(accessCode);
                    List<CreateStandardNoModel> resultSet = new ArrayList<>();
                    modelList.forEach(mod -> {
                        CreateStandardNoModel result = new CreateStandardNoModel();
                        result.setAccessCode(mod.getAccessCode());
                        result.setCreatedBy(mod.getCreatedBy());
                        result.setCreatedDate(mod.getCreatedDate());
                        result.setNumberType(mod.getNumberType());
                        result.setNumberSubType(mod.getNumberSubType());
                        result.setBulkUploadId("");
                        result.setCreatedStandardNumberId(mod.getCreatedStandardNumberId());
                        result.setCoverageArea(mod.getCoverageArea());
                        result.setMinimumNumber(accessCode + mod.getMinimumNumber()); // concat access and min no
                        result.setMaximumNumber(accessCode + mod.getMaximumNumber()); // concat access and max no
                        resultSet.add(result);
                    });

                    log.info("Result set from assembling {} ====> ", resultSet);
                    if (resultSet != null) {
                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                        genericResponse.setOutputPayload(resultSet);
                    } else {
                        genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
                    }
                } catch (Exception exception) {
                    log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
                    genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
                }
                return genericResponse;

            case "GEOGRAPHICAL":
                try {
                    // Get available number block for national number for specific access code
                    List<CreateStandardNoModel> modelList = standardNoRepository.getAvailableGeographicalNo(accessCode);
                    List<CreateStandardNoModel> resultSet = new ArrayList<>();
                    modelList.forEach(mod -> {
                        CreateStandardNoModel result = new CreateStandardNoModel();
                        result.setAccessCode(mod.getAccessCode());
                        result.setCreatedBy(mod.getCreatedBy());
                        result.setCreatedDate(mod.getCreatedDate());
                        result.setNumberType(mod.getNumberType());
                        result.setNumberSubType(mod.getNumberSubType());
                        result.setBulkUploadId("");
                        result.setCreatedStandardNumberId(mod.getCreatedStandardNumberId());
                        result.setCoverageArea(mod.getCoverageArea());
//                        result.setMinimumNumber(accessCode + mod.getMinimumNumber()); // concat access and min no
//                        result.setMaximumNumber(accessCode + mod.getMaximumNumber()); // concat access and max no
                        result.setMinimumNumber(mod.getMinimumNumber());
                        result.setMaximumNumber(mod.getMaximumNumber());

                        resultSet.add(result);
                    });


                    log.info("Result set from assembling {} ====> ", resultSet);
                    if (resultSet != null) {
                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                        genericResponse.setOutputPayload(resultSet);
                    } else {
                        genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
                    }
                } catch (Exception exception) {
                    log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
                    genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
                }
                return genericResponse;

            case "ISPC":
                try {
                    List<CreateIspcNoModel> modelList = ispcRepository.getAvailableIspcNumber();
                    log.info("Result set from repository {} ====> ", modelList);
                    if (modelList != null) {
                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                        genericResponse.setOutputPayload(modelList);
                    } else {
                        genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
                    }
                } catch (Exception exception) {
                    log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
                    genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
                }
                return genericResponse;
            case "VANITY":
                try {
                    List<CreateSpecialNoModel> modelList = specialRepository.getAvailableVanityNumbers(accessCode);
                    log.info("Result set from repository {} ====> ", modelList);
                    if (modelList != null) {
                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                        genericResponse.setOutputPayload(modelList);
                    } else {
                        genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
                    }
                } catch (Exception exception) {
                    log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
                    genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
                }
                return genericResponse;

            case "TOLL-FREE":
                try {
                    List<CreateSpecialNoModel> modelList = specialRepository.getAvailableTollFreeNumbers(accessCode);
                    log.info("Result set from repository {} ====> ", modelList);
                    if (modelList != null) {
                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                        genericResponse.setOutputPayload(modelList);
                    } else {
                        genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
                    }
                } catch (Exception exception) {
                    log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
                    genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
                }
                return genericResponse;
            case "SHORT-CODE":
                try {
                    List<CreateShortCodeNoModel> modelList = shortCodeRepository.getAvailableShortCodeNumber(serviceName);
                    log.info("Result set from repository {} ====> ", modelList);
                    if (modelList != null) {
                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                        genericResponse.setOutputPayload(modelList);
                    } else {
                        genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
                    }
                } catch (Exception exception) {
                    log.info("Exception occurred !!!!!!!! {} ", exception.getMessage());
                    genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
                }
                return genericResponse;

            default:
                break;
        }
        return null;
    }
}


