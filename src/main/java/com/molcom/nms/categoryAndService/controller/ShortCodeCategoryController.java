package com.molcom.nms.categoryAndService.controller;

import com.molcom.nms.categoryAndService.dto.ShortCodeCategoryModel;
import com.molcom.nms.categoryAndService.service.ShortCodeCategoryService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/shortCodeCategory")
public class ShortCodeCategoryController {
    @Autowired
    private ShortCodeCategoryService service;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Save short code category
     *
     * @param model
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody ShortCodeCategoryModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.save(model);
        }
    }

    /**
     * get all
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
     * delete short code category
     *
     * @param categoryName
     * @param authorization
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> deleteShortCodeCategory(String categoryName,
                                                      @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return service.delete(categoryName);
        }
    }
}
