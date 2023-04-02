package com.molcom.nms.number.renewal.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.number.renewal.service.INumRenewalService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/numRenewal")
public class NumberRenewalController {
    @Autowired
    private INumRenewalService numRenewalService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to get billing record by application id
     *
     * @param renewalId Unique field for a billing record
     * @return
     * @throws Exception
     */
    @GetMapping("/getByRenewalId")
    public GenericResponse<?> getByRenewalId(@RequestParam int renewalId,
                                             @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return numRenewalService.getByRenewalId(renewalId);
        }
    }


    /**
     * Endpoint to filter for regular user
     * Filters are: ApplicationId, StartBillingPeriod, EndBillingPeriod, DateAllocatedFrom and DateAllocatedTo
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/filterForRegularUser")
    public GenericResponse<?> filterForRegularUser(@RequestHeader("authorization") String authorization,
                                                   @RequestParam String queryParam1,
                                                   @RequestParam String queryValue1,
                                                   @RequestParam String queryParam2,
                                                   @RequestParam String queryValue2,
                                                   @RequestParam String queryParam3,
                                                   @RequestParam String queryValue3,
                                                   @RequestParam String queryParam4,
                                                   @RequestParam String queryValue4,
                                                   @RequestParam String queryParam5,
                                                   @RequestParam String queryValue5,
                                                   @RequestParam String companyName,
                                                   @RequestParam String rowNumber) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return numRenewalService.filterForRegularUser(queryParam1, queryValue1, queryParam2, queryValue2, queryParam3
                    , queryValue3, queryParam4, queryValue4, queryParam5, queryValue5, companyName, rowNumber);
        }
    }

    /**
     * Endpoint to filter for admin user
     * Filters are: ApplicationId, StartBillingPeriod, EndBillingPeriod, DateAllocatedFrom, DateAllocatedTo
     * Organization and PaymentStatus
     * // TODO Date search check for DateAllocatedFrom, DateAllocatedTo, StartBillingPeriod and EndBillingPeriod
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/filterForAdminUser")
    public GenericResponse<?> filterForAdminUser(@RequestHeader("authorization") String authorization,
                                                 @RequestParam String queryParam1,
                                                 @RequestParam String queryValue1,
                                                 @RequestParam String queryParam2,
                                                 @RequestParam String queryValue2,
                                                 @RequestParam String queryParam3,
                                                 @RequestParam String queryValue3,
                                                 @RequestParam String queryParam4,
                                                 @RequestParam String queryValue4,
                                                 @RequestParam String queryParam5,
                                                 @RequestParam String queryValue5,
                                                 @RequestParam String queryParam6,
                                                 @RequestParam String queryValue6,
                                                 @RequestParam String queryParam7,
                                                 @RequestParam String queryValue7,
                                                 @RequestParam String rowNumber) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return numRenewalService.filterForAdminUser(queryParam1, queryValue1, queryParam2, queryValue2, queryParam3
                    , queryValue3, queryParam4, queryValue4, queryParam5, queryValue5,
                    queryParam6, queryValue6, queryParam7, queryValue7, rowNumber);
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
            return numRenewalService.getAll();
        }
    }

}
