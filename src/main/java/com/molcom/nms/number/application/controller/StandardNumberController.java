package com.molcom.nms.number.application.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.application.dto.StandardNumberModel;
import com.molcom.nms.number.application.dto.StandardNumberResponse;
import com.molcom.nms.number.application.service.IStandardNumberService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/standardNumber")
public class StandardNumberController {
    @Autowired
    private IStandardNumberService standardNumberService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to save first step special number creation
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping("/firstStep")
    public GenericResponse<?> saveStandardNoIst(@RequestBody StandardNumberModel model,
                                                @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return standardNumberService.saveStandardNoFistStep(model);
        }
    }

    /**
     * Endpoint to save second step special number creation
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping("/secondStep")
    public GenericResponse<?> saveStandardNo2nd(@RequestBody StandardNumberModel model,
                                                @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return standardNumberService.saveStandardNoSecondStep(model);
        }
    }

    /**
     * Endpoint to save third step special number creation
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping("/thirdStep")
    public GenericResponse<?> saveStandardNoo3rd(@RequestBody StandardNumberModel model,
                                                 @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return standardNumberService.saveStandardNoThirdStep(model);
        }
    }

    /**
     * @param currentStep
     * @param applicationId
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping("/docStep")
    public GenericResponse<?> saveSpecialDocStep(@RequestParam String currentStep,
                                                 @RequestParam String applicationId,
                                                 @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return standardNumberService.saveStandardDocStep(currentStep, applicationId);
        }
    }

    /**
     * Endpoint to save filter existing special numbers
     * Filters are: ApplicationId, SubType, ApplicationType, CoverageArea, AreaCode, AccessCode, ApplicationStatus,
     * ApplicationPaymentStatus, AllocationPaymentStatus, {CreatedDateFrom ** (StartDate)} and {CreatedDateTo ** (EndDate)}
     * Use date without timestamp
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/filterSpecialNumber")
    public GenericResponse<?> filterForNonAdminStandardNo(
            @RequestParam String CompanyName,
            @RequestHeader("authorization") String authorization,
            @RequestParam String ApplicationId,
            @RequestParam String SubType,
            @RequestParam String ApplicationType,
            @RequestParam String CoverageArea,
            @RequestParam String AreaCode,
            @RequestParam String AccessCode,
            @RequestParam String ApplicationStatus,
            @RequestParam String ApplicationPaymentStatus,
            @RequestParam String AllocationPaymentStatus,
            @RequestParam String StartDate,
            @RequestParam String EndDate,
            @RequestParam String rowNumber) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return standardNumberService.filterStandardNo(CompanyName, ApplicationId, SubType, ApplicationType, CoverageArea, AreaCode
                    , AccessCode, ApplicationStatus, ApplicationPaymentStatus, AllocationPaymentStatus, StartDate
                    , EndDate, rowNumber);
        }
    }

    /**
     * @param authorization
     * @param CompanyName
     * @param ApplicationId
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
    @GetMapping("/filterAdminSpecialNumber")
    public GenericResponse<?> filterForStandardNo(
            @RequestHeader("authorization") String authorization,
            @RequestParam String CompanyName,
            @RequestParam String ApplicationId,
            @RequestParam String SubType,
            @RequestParam String ApplicationType,
            @RequestParam String CoverageArea,
            @RequestParam String AreaCode,
            @RequestParam String AccessCode,
            @RequestParam String ApplicationStatus,
            @RequestParam String ApplicationPaymentStatus,
            @RequestParam String AllocationPaymentStatus,
            @RequestParam String StartDate,
            @RequestParam String EndDate,
            @RequestParam String rowNumber) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return standardNumberService.filterAdminStandardNo(ApplicationId, CompanyName, SubType, ApplicationType, CoverageArea, AreaCode
                    , AccessCode, ApplicationStatus, ApplicationPaymentStatus, AllocationPaymentStatus, StartDate
                    , EndDate, rowNumber);
        }
    }

    /**
     * Endpoint to get special number by application id
     *
     * @param applicationId
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/getStandardNoById")
    public StandardNumberResponse getStandardNoById(@RequestParam String applicationId,
                                                    @RequestParam String userId,
                                                    @RequestHeader("authorization") String authorization) throws Exception {
        return standardNumberService.getStandardNoById(applicationId, userId);
    }

    /**
     * delete
     *
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> delete(@RequestParam String applicationId,
                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return standardNumberService.deleteApplication(applicationId);
        }
    }

    /**
     * @param applicationId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getFirstStep")
    public GenericResponse<?> getFirstStep(@RequestParam String applicationId,
                                           @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return standardNumberService.getFistStep(applicationId);
        }
    }

    /**
     * @param applicationId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getSecondStep")
    public GenericResponse<?> getSecondStep(@RequestParam String applicationId,
                                            @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return standardNumberService.getSecondStep(applicationId);
        }

    }

    /**
     * @param applicationId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getForthStep")
    public GenericResponse<?> getForthStep(@RequestParam String applicationId,
                                           @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return standardNumberService.getFourthStep(applicationId);
        }
    }

    /**
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getAll")
    public GenericResponse<?> getAll(@RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return standardNumberService.getAll();
        }
    }
}

