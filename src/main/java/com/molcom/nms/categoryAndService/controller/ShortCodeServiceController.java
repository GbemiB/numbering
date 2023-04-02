package com.molcom.nms.categoryAndService.controller;

import com.molcom.nms.categoryAndService.dto.ShortCodeServiceModel;
import com.molcom.nms.categoryAndService.service.ShortCodeServiceService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/shortCodeService")
public class ShortCodeServiceController {
    @Autowired
    private ShortCodeServiceService service;

    @Autowired
    private TokenHandler tokenHandler;


    /**
     * save short code service
     *
     * @param model
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody ShortCodeServiceModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.save(model);
        }
    }

    /**
     * get all short code services
     *
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping()
    public GenericResponse<?> getAll(@RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.getAll();
        }
    }

    /**
     * get service by service name
     *
     * @param categoryName
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getByCategoryName")
    public GenericResponse<?> getByServiceName(String categoryName,
                                               @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.getByCategoryName(categoryName);
        }
    }

    /**
     * delete service by service name
     *
     * @param serviceName
     * @param shortCodeCategory
     * @param authorization
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> deleteShortCodeService(@RequestParam String serviceName,
                                                     @RequestParam String shortCodeCategory,
                                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.delete(serviceName, shortCodeCategory);
        }

    }
}
