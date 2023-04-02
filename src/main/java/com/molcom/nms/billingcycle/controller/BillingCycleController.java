package com.molcom.nms.billingcycle.controller;

import com.molcom.nms.billingcycle.dto.BillingCycleModel;
import com.molcom.nms.billingcycle.service.BillingCycleService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/billingCycle")
public class BillingCycleController {

    @Autowired
    private BillingCycleService billingCycleService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Save
     *
     * @param model
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody BillingCycleModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return billingCycleService.save(model);
        }
    }

    /**
     * Edit
     *
     * @param model
     * @param billingId
     * @param authorization
     * @return
     * @throws Exception
     */
    @PatchMapping()
    public GenericResponse<?> edit(@RequestBody BillingCycleModel model,
                                   @RequestParam int billingId,
                                   @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return billingCycleService.edit(model, billingId);
        }
    }

    /**
     * delete
     *
     * @param billingId
     * @param authorization
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> delete(@RequestParam int billingId,
                                     @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return billingCycleService.deleteById(billingId);
        }
    }

    /**
     * get by coverage id
     *
     * @param billingId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getByCoverageId")
    public GenericResponse<?> getById(@RequestParam int billingId,
                                      @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return billingCycleService.findById(billingId);
        }
    }

    /**
     * get all
     *
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("getAll")
    public GenericResponse<?> getAll(@RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return billingCycleService.getAll();
        }
    }

    /**
     * filter
     *
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param rowNumber
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/filter")
    public GenericResponse<?> filter(@RequestParam String queryParam1,
                                     @RequestParam String queryValue1,
                                     @RequestParam String queryParam2,
                                     @RequestParam String queryValue2,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return billingCycleService.filter(queryParam1, queryValue1, queryParam2, queryValue2, rowNumber);
        }
    }
}

