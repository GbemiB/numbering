package com.molcom.nms.signatory.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.security.TokenHandler;
import com.molcom.nms.signatory.dto.SignatoryModel;
import com.molcom.nms.signatory.service.ISignatoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/signatory")
public class SignatoryController {

    @Autowired
    private ISignatoryService signatoryService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * @param model
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody SignatoryModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return signatoryService.save(model);
        }

    }

    /**
     * @param model
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping("/saveForApplication")
    public GenericResponse<?> saveForApp(@RequestBody SignatoryModel model,
                                         @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return signatoryService.saveSignatoryToApplication(model);
        }
    }


    /**
     * @param model
     * @param signatoryId
     * @param authorization
     * @return
     * @throws Exception
     */
    @PatchMapping()
    public GenericResponse<?> edit(@RequestBody SignatoryModel model,
                                   @RequestParam int signatoryId,
                                   @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return signatoryService.edit(model, signatoryId);
        }
    }

    /**
     * @param signatoryId
     * @param authorization
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> delete(@RequestParam int signatoryId,
                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return signatoryService.deleteById(signatoryId);
        }
    }


    /**
     * @param signatoryId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getBySignatoryId")
    public GenericResponse<?> getByUserId(@RequestParam int signatoryId,
                                          @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return signatoryService.findByID(signatoryId);
        }
    }


    /**
     * @param rowNumber
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getAll")
    public GenericResponse<?> getAll(@RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return signatoryService.getAll(rowNumber);
        }
    }

    /**
     * @param signatoryId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/setActive")
    public GenericResponse<?> setSignatoryAsActive(@RequestParam int signatoryId,
                                                   @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return signatoryService.saveActive(signatoryId);
        }
    }

    /**
     * @param signatoryId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/removeActive")
    public GenericResponse<?> removeSignatoryAsActive(@RequestParam int signatoryId,
                                                      @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return signatoryService.removeActive(signatoryId);
        }
    }

    /**
     * @param applicationId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getForApplication")
    public GenericResponse<?> findAllForApplication(@RequestParam String applicationId,
                                                    @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return signatoryService.findSavedSignatory(applicationId);
        }
    }


    /**
     * @param signatoryName
     * @param signatoryDesignation
     * @param rowNumber
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/filter")
    public GenericResponse<?> filter(@RequestParam String signatoryName,
                                     @RequestParam String signatoryDesignation,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return signatoryService.filter(signatoryName, signatoryDesignation, rowNumber);
        }
    }
}