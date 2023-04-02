package com.molcom.nms.number.selectedNumber.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
import com.molcom.nms.number.selectedNumber.service.ISelectedNumbersService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "nms/selectedNumbers")
public class SelectedNumbersController {

    @Autowired
    private ISelectedNumbersService selectedNumbersService;

    @Autowired
    private TokenHandler tokenHandler;


    /**
     * Endpoint to add selected number
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> saveSelectedNumber(@RequestBody SelectedNumberModel model,
                                                 @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return selectedNumbersService.saveSelectedNumber(model);
        }
    }

    @PostMapping("/saveMultiple")
    public GenericResponse<?> saveSelectedNumber(@RequestBody List<SelectedNumberModel> model,
                                                 @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return selectedNumbersService.saveBulkSelectedNumber(model);
        }
    }


    /**
     * Endpoint to delete selected number
     *
     * @param selectedNumberId
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> deleteSelectedNumber(@RequestParam String selectedNumberId,
                                                   @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return selectedNumbersService.deleteSelectedNumber(selectedNumberId);
        }
    }


    /**
     * Endpoint to get selected numbers by application id
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @GetMapping()
    public GenericResponse<?> getByApplicationId(@RequestParam String applicationId,
                                                 @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return selectedNumbersService.getSelectedNumbers(applicationId);
        }
    }


    /**
     * @param applicationId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/dropRejectedNumbers")
    public GenericResponse<?> dropRejectedNumbers(@RequestParam String applicationId,
                                                  @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return selectedNumbersService.dropRejectedNumbers(applicationId);
        }
    }

    /**
     * @param model
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping("/replaceRejectedNumber")
    public GenericResponse<?> replaceRejectedNumbers(@RequestBody List<SelectedNumberModel> model,
                                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return selectedNumbersService.replaceSelectedNumber(model);
        }
    }
}
