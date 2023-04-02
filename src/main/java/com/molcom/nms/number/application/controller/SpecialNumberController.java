package com.molcom.nms.number.application.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.application.dto.SpecialNumberModel;
import com.molcom.nms.number.application.dto.SpecialNumberResponse;
import com.molcom.nms.number.application.service.ISpecialNumberService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/specialNumber")
public class SpecialNumberController {

    @Autowired
    private ISpecialNumberService specialNumberService;

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
    public GenericResponse<?> saveSpecialNoIst(@RequestBody SpecialNumberModel model,
                                               @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return specialNumberService.saveSpecialNoFistStep(model);
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
    public GenericResponse<?> saveSpecialNo2nd(@RequestBody SpecialNumberModel model,
                                               @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return specialNumberService.saveSpecialNoSecondStep(model);
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
    public GenericResponse<?> saveSpecialNo3rd(@RequestBody SpecialNumberModel model,
                                               @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return specialNumberService.saveSpecialNoThirdStep(model);
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
            return specialNumberService.saveSpecialDocStep(currentStep, applicationId);
        }
    }

    /**
     * Endpoint to save filter existing special numbers
     * Filters are: ApplicationId, SubType, ApplicationType, AccessCode, ApplicationStatus,
     * ApplicationPaymentStatus, AllocationPaymentStatus, {CreatedDateFrom ** (StartDate)} and {CreatedDateTo ** (EndDate)}
     * Use date without timestamp
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/filterSpecialNumber")
    public GenericResponse<?> filterForSpecialNo(
            @RequestParam String CompanyName,
            @RequestParam String ApplicationId,
            @RequestParam String SubType,
            @RequestParam String ApplicationType,
            @RequestParam String AccessCode,
            @RequestParam String ApplicationStatus,
            @RequestParam String ApplicationPaymentStatus,
            @RequestParam String AllocationPaymentStatus,
            @RequestParam String StartDate,
            @RequestParam String EndDate,
            @RequestParam String rowNumber,
            @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return specialNumberService.filterSpecialNo(CompanyName, ApplicationId, SubType, ApplicationType, AccessCode,
                    ApplicationStatus, ApplicationPaymentStatus, AllocationPaymentStatus, StartDate,
                    EndDate, rowNumber);
        }
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
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/filterAdminSpecialNumber")
    public GenericResponse<?> filterForAdminSpecialNo(
            @RequestParam String CompanyName,
            @RequestParam String ApplicationId,
            @RequestParam String SubType,
            @RequestParam String ApplicationType,
            @RequestParam String AccessCode,
            @RequestParam String ApplicationStatus,
            @RequestParam String ApplicationPaymentStatus,
            @RequestParam String AllocationPaymentStatus,
            @RequestParam String StartDate,
            @RequestParam String EndDate,
            @RequestParam String rowNumber,
            @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return specialNumberService.filterAdminSpecialNo(CompanyName, ApplicationId, SubType, ApplicationType, AccessCode,
                    ApplicationStatus, ApplicationPaymentStatus, AllocationPaymentStatus, StartDate,
                    EndDate, rowNumber);
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
    @GetMapping("/getSpecialById")
    public SpecialNumberResponse getSpecialById(@RequestParam String applicationId,
                                                @RequestParam String userId,
                                                @RequestHeader("authorization") String authorization) throws Exception {
        return specialNumberService.getSpecialNoById(applicationId, userId);

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
            return specialNumberService.deleteApplication(applicationId);
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
            return specialNumberService.getFistStep(applicationId);
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
            return specialNumberService.getSecondStep(applicationId);
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
            return specialNumberService.getFourthStep(applicationId);
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
            return specialNumberService.getAll();
        }
    }
}

