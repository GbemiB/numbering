package com.molcom.nms.number.application.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.application.dto.ShortCodeModel;
import com.molcom.nms.number.application.dto.ShortCodeResponse;
import com.molcom.nms.number.application.service.IShortCodeService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/shortCode")
public class ShortCodeController {

    @Autowired
    private IShortCodeService shortCodeService;

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
    public GenericResponse<?> saveShortCodeIst(@RequestBody ShortCodeModel model,
                                               @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return shortCodeService.saveShortCodeAppFistStep(model);
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
    public GenericResponse<?> saveShortCode2nd(@RequestBody ShortCodeModel model,
                                               @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return shortCodeService.saveShortCodeAppSecondStep(model);
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
    public GenericResponse<?> saveShortCode3rd(@RequestBody ShortCodeModel model,
                                               @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return shortCodeService.saveShortCodeAppThirdStep(model);
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
    public GenericResponse<?> saveShortCodeDocStep(@RequestParam String currentStep,
                                                   @RequestParam String applicationId,
                                                   @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return shortCodeService.saveShortCodeDocStep(currentStep, applicationId);
        }
    }

    /**
     * Endpoint to filter for short codes
     * Filters are: ApplicationId, ShortCodeCategory, ShortCodeServiceService, ApplicationType, ApplicationStatus,
     * ApplicationPaymentStatus, AllocationPaymentStatus, {CreatedDateFrom ** (StartDate)} and {CreatedDateTo ** (EndDate)}
     * Use date without timestamp
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/filterShortCodes")
    public GenericResponse<?> filterForShortCodes(
            @RequestParam String CompanyName,
            @RequestParam String ApplicationId,
            @RequestParam String ShortCodeCategory,
            @RequestParam String ShortCodeService,
            @RequestParam String ApplicationType,
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
            return shortCodeService.filterShortCodes(CompanyName, ApplicationId, ShortCodeCategory
                    , ShortCodeService, ApplicationType, ApplicationStatus,
                    ApplicationPaymentStatus, AllocationPaymentStatus, StartDate,
                    EndDate, rowNumber);
        }
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
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/filterAdminShortCodes")
    public GenericResponse<?> filterForAdminShortCodes(
            @RequestParam String CompanyName,
            @RequestParam String ApplicationId,
            @RequestParam String ShortCodeCategory,
            @RequestParam String ShortCodeService,
            @RequestParam String ApplicationType,
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
            return shortCodeService.filterAdminShortCodes(CompanyName, ApplicationId, ShortCodeCategory
                    , ShortCodeService, ApplicationType, ApplicationStatus,
                    ApplicationPaymentStatus, AllocationPaymentStatus, StartDate,
                    EndDate, rowNumber);
        }
    }

    /**
     * Endpoint to get short code by application id
     *
     * @param applicationId
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/getShortCodeById")
    public ShortCodeResponse getShortCodeById(@RequestParam String applicationId,
                                              @RequestParam String userId,
                                              @RequestHeader("authorization") String authorization) throws Exception {
        return shortCodeService.getShortCodeById(applicationId, userId);

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
            return shortCodeService.deleteApplication(applicationId);
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
            return shortCodeService.getFistStep(applicationId);
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
            return shortCodeService.getSecondStep(applicationId);
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
            return shortCodeService.getFourthStep(applicationId);
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
            return shortCodeService.getAll();
        }
    }
}
