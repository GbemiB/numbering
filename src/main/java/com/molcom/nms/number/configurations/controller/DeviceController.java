package com.molcom.nms.number.configurations.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.configurations.dto.DeviceModel;
import com.molcom.nms.number.configurations.service.IDeviceService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/device")
public class DeviceController {
    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private TokenHandler tokenHandler;


    /**
     * Endpoint to add device service
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> saveDevice(@RequestBody DeviceModel model,
                                         @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return deviceService.saveDevice(model);
        }
    }


    /**
     * Endpoint to delete device service
     *
     * @param deviceId
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> deleteDevice(@RequestParam String deviceId,
                                           @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return deviceService.deleteDevice(deviceId);
        }
    }


    /**
     * Endpoint to get device service by application id
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
            return deviceService.getDevice(applicationId);
        }
    }

}
