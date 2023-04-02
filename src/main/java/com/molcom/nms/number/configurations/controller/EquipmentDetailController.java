package com.molcom.nms.number.configurations.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.configurations.dto.EquipmentDetailModel;
import com.molcom.nms.number.configurations.service.IEquipmentDetailsService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/equipmentDetails")
public class EquipmentDetailController {
    @Autowired
    private IEquipmentDetailsService equipmentDetailsService;

    @Autowired
    private TokenHandler tokenHandler;


    /**
     * Endpoint to add equipment detail
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> saveEquipDetail(@RequestBody EquipmentDetailModel model,
                                              @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return equipmentDetailsService.saveEquipmentDetail(model);
        }
    }


    /**
     * Endpoint to delete equipment detail
     *
     * @param equipmentId
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> deleteEquipDetail(@RequestParam String equipmentId,
                                                @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return equipmentDetailsService.deleteEquipmentDetail(equipmentId);
        }
    }


    /**
     * Endpoint to get equipment detail by application id
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @GetMapping()
    public GenericResponse<?> getByApplicationId(@RequestParam String applicationId,
                                                 @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return equipmentDetailsService.getEquipmentDetail(applicationId);
        }
    }
}
