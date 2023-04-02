package com.molcom.nms.GenericDatabaseUpdates.controller;

import com.molcom.nms.GenericDatabaseUpdates.service.IGenericTableUpdateService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "nms/genericUpdate")
public class GenericUpdateController {
    @Autowired
    private IGenericTableUpdateService genericTableUpdateService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Generic table update
     *
     * @param tableName
     * @param columnName
     * @param columnValue
     * @param columnConstraintKey
     * @param columnConstraintValue
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping("/updateColomn")
    public GenericResponse<?> update(String tableName, String columnName, String columnValue, String columnConstraintKey, String columnConstraintValue,
                                     @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return genericTableUpdateService.updateTableColumn(tableName, columnName, columnValue, columnConstraintKey, columnConstraintValue);

        }
    }

}
