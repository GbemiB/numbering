package com.molcom.nms.fee.selectedFeeSchedule.controller;

import com.molcom.nms.fee.selectedFeeSchedule.dto.SelectedFeeScheduleModel;
import com.molcom.nms.fee.selectedFeeSchedule.service.ISelectedFeeScheduleService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/selectedFeeSchedule")
public class SelectedFeeScheduleController {

    @Autowired
    private ISelectedFeeScheduleService selectedFeeScheduleService;

    @Autowired
    private TokenHandler tokenHandler;

    @PostMapping()
    public GenericResponse<?> save(@RequestBody SelectedFeeScheduleModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return selectedFeeScheduleService.save(model);
        }
    }

    @GetMapping("/getByApplicationId")
    public GenericResponse<?> getByApplicationId(String applicationId,
                                                 @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return selectedFeeScheduleService.findByApplicationId(applicationId);
        }
    }
}
