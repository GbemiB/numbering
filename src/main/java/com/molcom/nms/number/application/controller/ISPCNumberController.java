package com.molcom.nms.number.application.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.number.application.dto.ISPCNumberModel;
import com.molcom.nms.number.application.dto.ISPCNumberResponse;
import com.molcom.nms.number.application.service.IISPCNumberService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/ISPCNumbers")
public class ISPCNumberController {
    @Autowired
    private IISPCNumberService iispcNumberService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint save first step short code application
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping("/firstStep")
    public GenericResponse<?> saveISPCIst(@RequestBody ISPCNumberModel model,
                                          @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return iispcNumberService.saveISPCNumberFistStep(model);
        }
    }

    /**
     * Endpoint save second step short code application
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping("/secondStep")
    public GenericResponse<?> saveISPC2nd(@RequestBody ISPCNumberModel model,
                                          @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return iispcNumberService.saveISPCNumberSecondStep(model);
        }
    }

    /**
     * Endpoint save third step short code application
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping("/thirdStep")
    public GenericResponse<?> saveISPC3rd(@RequestBody ISPCNumberModel model,
                                          @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return iispcNumberService.saveISPCNumberThirdStep(model);
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
    public GenericResponse<?> saveISPCStep(@RequestParam String currentStep,
                                           @RequestParam String applicationId,
                                           @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return iispcNumberService.saveISPCNumberDocStep(currentStep, applicationId);
        }
    }

    /**
     * Endpoint to filter for short codes
     * Filters are: ApplicationId, ApplicationType, ApplicationStatus
     * {CreatedDateFrom ** (StartDate)} and {CreatedDateTo ** (EndDate)}
     * Use date without timestamp
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/filterISPC")
    public GenericResponse<?> filterForIpsc(
            @RequestParam String CompanyName,
            @RequestParam String ApplicationId,
            @RequestParam String ApplicationType,
            @RequestParam String ApplicationStatus,
            @RequestParam String StartDate,
            @RequestParam String EndDate,
            @RequestParam String rowNumber,
            @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return iispcNumberService.filterISPCNumber(CompanyName, ApplicationId, ApplicationType, ApplicationStatus, StartDate, EndDate, rowNumber);

        }
    }

    /**
     * @param CompanyName
     * @param ApplicationId
     * @param ApplicationType
     * @param ApplicationStatus
     * @param StartDate
     * @param EndDate
     * @param rowNumber
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/filterAdminISPC")
    public GenericResponse<?> filterForAdminIspc(
            @RequestParam(required = false) String CompanyName,
            @RequestParam(required = false) String ApplicationId,
            @RequestParam(required = false) String ApplicationType,
            @RequestParam(required = false) String ApplicationStatus,
            @RequestParam(required = false) String StartDate,
            @RequestParam(required = false) String EndDate,
            @RequestParam(required = false) String rowNumber,
            @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return iispcNumberService.filterAdminISPCNumber(CompanyName, ApplicationId, ApplicationType, ApplicationStatus, StartDate, EndDate, rowNumber);

        }
    }

    /**
     * Endpoint to get ISPC by application id
     *
     * @param applicationId
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/getISPCNumberById")
    public ISPCNumberResponse getIspcById(@RequestParam String applicationId,
                                          @RequestParam String userId,
                                          @RequestHeader("authorization") String authorization) throws Exception {
        return iispcNumberService.getISPCNumberById(applicationId, userId);

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
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return iispcNumberService.deleteApplication(applicationId);
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
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return iispcNumberService.getFistStep(applicationId);
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
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return iispcNumberService.getSecondStep(applicationId);
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
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return iispcNumberService.getFourthStep(applicationId);
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
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return iispcNumberService.getAll();
        }
    }

}
