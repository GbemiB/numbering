package com.molcom.nms.number.configurations.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.GetUniqueId;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.number.configurations.dto.SupportingDocument;
import com.molcom.nms.number.configurations.service.ISupportingDocumentService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "nms/supportingDoc")
public class SupportingDocumentController {

    @Autowired
    private ISupportingDocumentService supportingDocumentService;

    @Autowired
    private TokenHandler tokenHandler;
    /**
     * Endpoint to add supporting document
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> saveSupportingDoc(@RequestBody SupportingDocument model,
                                                @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return supportingDocumentService.saveSupportingDoc(model);
        }
    }


    /**
     * Endpoint to update supporting document
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PatchMapping()
    public GenericResponse<?> updateSupportingDoc(@RequestBody SupportingDocument model,
                                                  @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return supportingDocumentService.updateSupportingDoc(model);
        }
    }

    /**
     * Endpoint to delete supporting document
     *
     * @param documentId
     * @return
     * @throws Exception
     */
    @DeleteMapping("/deleteByDocumentId")
    public GenericResponse<?> deleteAdditionalDocById(@RequestParam String documentId,
                                                      @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return supportingDocumentService.deleteSupportingDocById(documentId);
        }
    }


    @DeleteMapping("/deleteByDocumentName")
    public GenericResponse<?> deleteAdditionalDocByName(@RequestParam String documentName,
                                                        @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return supportingDocumentService.deleteSupportingDocByName(documentName);
        }
    }


    /**
     * Endpoint to get required supporting document by application id
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @GetMapping("/getRequiredDoc")
    public GenericResponse<?> getByRequiredDocByApplicationId(@RequestParam String applicationId,
                                                              @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return supportingDocumentService.getRequiredSupportingDoc(applicationId);
        }
    }

    /**
     * Endpoint to get required additional document by application id
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @GetMapping("/getAdditionalDoc")
    public GenericResponse<?> getByAdditionalDocByApplicationId(@RequestParam String applicationId,
                                                                @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return supportingDocumentService.getAdditionalSupportingDoc(applicationId);
        }
    }

    /**
     * Endpoint to get required additional document by application id
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @GetMapping("/getAllDocPerApplication")
    public GenericResponse<?> getAllDocPerApplicationId(@RequestParam String applicationId,
                                                        @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return supportingDocumentService.getAllDocumentByApplication(applicationId);
        }
    }
}
