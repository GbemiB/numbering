package com.molcom.nms.fee.type.controller;

import com.molcom.nms.fee.type.dto.FeeTypeModel;
import com.molcom.nms.fee.type.service.IFeeTypeService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/feeType")
public class FeeTypeController {

    @Autowired
    private IFeeTypeService feeTypeService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to save new fee type
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody FeeTypeModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return feeTypeService.save(model);
        }
    }

    /**
     * Endpoint to save delete existing fee type
     *
     * @param feeTypeId
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> delete(@RequestParam int feeTypeId,
                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return feeTypeService.deleteById(feeTypeId);
        }
    }

    /**
     * Endpoint to get existing fee type by id
     *
     * @param feeTypeId
     * @return
     * @throws Exception
     */
    @GetMapping("/getByFeeTypeId")
    public GenericResponse<?> getById(@RequestParam int feeTypeId,
                                      @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return feeTypeService.findById(feeTypeId);
        }
    }

    /**
     * @return
     * @throws Exception
     */
    @GetMapping("/getAll")
    public GenericResponse<?> getAll(@RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return feeTypeService.getAll();
        }
    }

    /**
     * Endpoint to get filterForRegularUser existing fee types
     * Filter: FeeTypeName
     *
     * @param queryParam
     * @param queryValue
     * @param rowNumber
     * @return
     * @throws Exception
     */

    @GetMapping("/filter")
    public GenericResponse<?> filter(@RequestParam String queryParam,
                                     @RequestParam String queryValue,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return feeTypeService.filter(queryParam, queryValue, rowNumber);
        }
    }

}
