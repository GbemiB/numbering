package com.molcom.nms.numbertype.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.numbertype.dto.NumberTypeModel;
import com.molcom.nms.numbertype.service.INumberTypeService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/numberType")
public class NumberTypeController {

    @Autowired
    private INumberTypeService numberTypeService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * @param model
     * @param numberTypeId
     * @param authorization
     * @return
     * @throws Exception
     */
    @PatchMapping()
    public GenericResponse<?> update(@RequestBody NumberTypeModel model,
                                     @RequestParam int numberTypeId,
                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return numberTypeService.update(model, numberTypeId);
        }
    }

    /**
     * @param numberTypeId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getByNumberId")
    public GenericResponse<?> getById(@RequestParam int numberTypeId,
                                      @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return numberTypeService.findById(numberTypeId);
        }
    }

    /**
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("getAll")
    public GenericResponse<?> getAll(@RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return numberTypeService.getAll();
        }
    }

    /**
     * @param queryParam
     * @param queryValue
     * @param rowNumber
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/filter")
    public GenericResponse<?> filter(@RequestParam String queryParam,
                                     @RequestParam String queryValue,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return numberTypeService.filter(queryParam, queryValue, rowNumber);
        }
    }
}

