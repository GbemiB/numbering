package com.molcom.nms.filestorage.controller;

import com.molcom.nms.filestorage.dto.DocumentModel;
import com.molcom.nms.filestorage.dto.FileStorageModel;
import com.molcom.nms.filestorage.repository.FileStorageRepository;
import com.molcom.nms.filestorage.service.FileStorageService;
import com.molcom.nms.general.dto.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "nms/fileStorage/")
public class FileStorageController {

    private final FileStorageService fileStorageService;
    private final FileStorageRepository fileStorageRepository;

    public FileStorageController(FileStorageService fileStorageService, FileStorageRepository fileStorageRepository) {
        this.fileStorageService = fileStorageService;
        this.fileStorageRepository = fileStorageRepository;
    }

    @PostMapping(value = "save", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public List<GenericResponse<?>> save(@NotBlank @RequestParam("file") MultipartFile[] files,
                                         @RequestParam("type") String[] type,
                                         @NotBlank @RequestParam("applicationId") String applicationId) {
        log.info("File, type, application id {} {} {} ", files, type, applicationId);
        List<GenericResponse<?>> finalResponse = new ArrayList<>();
        List<DocumentModel> list = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            DocumentModel e = new DocumentModel();
            e.setFile(files[i]);
            e.setDocumentType(type[i]);
            list.add(e);
        }
        log.info("List of document {} ", list);
        list.forEach(e -> {
            try {
                if (!e.getFile().isEmpty()) {
                    GenericResponse<?> singleResponse = fileStorageService.uploadFile(e.getFile(), applicationId, e.getDocumentType());
                    log.info("Single response {} ", singleResponse);
                    finalResponse.add(singleResponse);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        log.info("Final response {} ", finalResponse);
        return finalResponse;
    }


    @PostMapping(value = "edit", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public List<GenericResponse<?>> edit(@NotBlank @RequestParam("file") MultipartFile[] files,
                                         @RequestParam("type") String[] type,
                                         @NotBlank @RequestParam("applicationId") String applicationId) {
        log.info("File, type, application id {} {} {} ", files, type, applicationId);
        List<GenericResponse<?>> finalResponse = new ArrayList<>();

        List<DocumentModel> list = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            DocumentModel e = new DocumentModel();
            e.setFile(files[i]);
            e.setDocumentType(type[i]);
            list.add(e);
        }
        log.info("List of document {} ", list);
        list.forEach(e -> {
            try {
                if (!e.getFile().isEmpty()) {
                    // check if the document exist
                    int doesDocumentExist = fileStorageRepository.fileCount(e.getDocumentType(), applicationId);
                    if (doesDocumentExist >= 1) {
                        //::::: Document exist :::::::::::
                        // delete the document record from data base
                        int isDeleted = fileStorageRepository.deleteByFileType(e.getDocumentType(), applicationId);
                        log.info("Delete code {} ", isDeleted);
                        if (isDeleted >= 1) {
                            // re-insert the document
                            GenericResponse<?> singleResponse = fileStorageService.uploadFile(e.getFile(), applicationId, e.getDocumentType());
                            log.info("Single response {} ", singleResponse);
                            finalResponse.add(singleResponse);
                        }
                    } else {
                        //::::: Document does not exist :::::::::::
                        // persist the document
                        GenericResponse<?> singleResponse = fileStorageService.uploadFile(e.getFile(), applicationId, e.getDocumentType());
                        log.info("Single response {} ", singleResponse);
                        finalResponse.add(singleResponse);
                    }
                }
            } catch (SQLException ex) {
                log.info("Exception occurred while editing document {} ", ex.getErrorCode());
            }
        });
        log.info("Final response {} ", finalResponse);
        return finalResponse;
    }


    @GetMapping("getFileById")
    public GenericResponse<FileStorageModel> getByFileId(@NotBlank @RequestParam int fileId) throws Exception {
        GenericResponse<FileStorageModel> genericResponse = new GenericResponse<>();
        FileStorageModel fileStorageModel = fileStorageRepository.getById(fileId);
        if (fileStorageModel != null) {
            genericResponse.setResponseCode("000");
            genericResponse.setResponseMessage("SUCCESS");
            genericResponse.setOutputPayload(fileStorageModel);
        } else {
            genericResponse.setResponseCode("666");
            genericResponse.setResponseMessage("No file found with  id " + fileId);
        }
        return genericResponse;
    }

    @GetMapping("getAllFilesForApplication")
    public GenericResponse<List<FileStorageModel>> getByApplicationId(@NotBlank @RequestParam String applicationId) throws Exception {
        GenericResponse<List<FileStorageModel>> genericResponse = new GenericResponse<>();
        List<FileStorageModel> list = fileStorageRepository.getByApplicationId(applicationId);
        if (list != null) {
            genericResponse.setResponseCode("000");
            genericResponse.setResponseMessage("SUCCESS");
            genericResponse.setOutputPayload(list);
        } else {
            genericResponse.setResponseCode("666");
            genericResponse.setResponseMessage("No file found for application id " + applicationId);
        }
        return genericResponse;
    }

    @DeleteMapping("deleteByFileId")
    public GenericResponse<?> deleteByFileId(@NotBlank @RequestParam String fileId) throws Exception {
        // TODO Delete the path on server also
        // check count if exist
        GenericResponse<List<FileStorageModel>> genericResponse = new GenericResponse<>();
        int isDeleted = fileStorageRepository.deleteByFileId(fileId);
        if (isDeleted >= 1) {
            genericResponse.setResponseCode("000");
            genericResponse.setResponseMessage("SUCCESS");
        } else {
            genericResponse.setResponseCode("666");
            genericResponse.setResponseMessage("Failed to delete file " + fileId);
        }
        return genericResponse;
    }

    @DeleteMapping("deleteAllFilesForApplication")
    // TODO Delete the path on server also
    public GenericResponse<?> deleteByApplicationId(@NotBlank @RequestParam String applicationId) throws Exception {
        GenericResponse<List<FileStorageModel>> genericResponse = new GenericResponse<>();
        int isDeleted = fileStorageRepository.deleteAll(applicationId);
        if (isDeleted >= 1) {
            genericResponse.setResponseCode("000");
            genericResponse.setResponseMessage("SUCCESS");
        } else {
            genericResponse.setResponseCode("666");
            genericResponse.setResponseMessage("Failed to delete all files for application id " + applicationId);
        }
        return genericResponse;
    }

}
