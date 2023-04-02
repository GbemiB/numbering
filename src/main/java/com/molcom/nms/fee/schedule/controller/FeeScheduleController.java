package com.molcom.nms.fee.schedule.controller;

import com.molcom.nms.fee.schedule.dto.FeeScheduleModel;
import com.molcom.nms.fee.schedule.service.IFeeScheduleService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/feeSchedule")
public class FeeScheduleController {

    @Autowired
    private IFeeScheduleService feeScheduleService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to add new fee schedule
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody FeeScheduleModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return feeScheduleService.save(model);
        }
    }

    /**
     * @param feeScheduleId
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> delete(@RequestParam int feeScheduleId,
                                     @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return feeScheduleService.deleteById(feeScheduleId);
        }
    }

    /**
     * Endpoint to edit existing fee schedule
     *
     * @param model
     * @param feeScheduleId
     * @return
     * @throws Exception
     */
    @PatchMapping()
    public GenericResponse<?> edit(@RequestBody FeeScheduleModel model,
                                   @RequestParam int feeScheduleId,
                                   @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return feeScheduleService.edit(model, feeScheduleId);
        }
    }

    /**
     * Endpoint to find existing fee schedule by id
     *
     * @param feeScheduleId
     * @return
     * @throws Exception
     */
    @GetMapping("/getByFeeScheduleId")
    public GenericResponse<?> getById(@RequestParam int feeScheduleId,
                                      @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return feeScheduleService.findById(feeScheduleId);
        }
    }

    @GetMapping("/getAll")
    public GenericResponse<?> getAll(@RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return feeScheduleService.getAll();
        }
    }

    @GetMapping("/getShortCodeFee")
    public GenericResponse<?> getShortCodeFee(@RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return feeScheduleService.getShortCodeFee();
        }
    }


    /**
     * Endpoint to filterForRegularUser existing fee schedule
     * Filters are: FeeScheduleName, NumberType and BillingStage
     *
     * @param queryParam1 FeeType, NumberType and BillingStage
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param queryParam3
     * @param queryValue3
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @GetMapping("/filter")
    public GenericResponse<?> filter(@RequestParam String queryParam1,
                                     @RequestParam String queryValue1,
                                     @RequestParam String queryParam2,
                                     @RequestParam String queryValue2,
                                     @RequestParam String queryParam3,
                                     @RequestParam String queryValue3,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return feeScheduleService.filter(queryParam1, queryValue1, queryParam2, queryValue2,
                    queryParam3, queryValue3, rowNumber);
        }
    }
}
