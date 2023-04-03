package com.molcom.nms.filestorage.service;

import com.molcom.nms.filestorage.dto.FileStorageModel;
import com.molcom.nms.filestorage.dto.FileStorageResponse;
import com.molcom.nms.filestorage.exception.FileStorageException;
import com.molcom.nms.filestorage.exception.MyFileNotFoundException;
import com.molcom.nms.filestorage.property.FileStorageProperties;
import com.molcom.nms.filestorage.repository.FileStorageRepository;
import com.molcom.nms.general.dto.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;


@Slf4j
@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final FileStorageRepository fileStorageRepository;

    @Value("${file.base.path}")
    private String fileBasePath;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties, FileStorageRepository fileStorageRepository) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.fileStorageRepository = fileStorageRepository;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }


    public GenericResponse<?> uploadFile(MultipartFile file, String applicationId, String type) throws SQLException {
        GenericResponse<FileStorageResponse> genericResponse = new GenericResponse<>();
        FileStorageModel model = new FileStorageModel();
        FileStorageResponse fileStorageResponse = new FileStorageResponse();

        // Todo create folder on server per application
        try {
            // Check if the file's name contains invalid characters
            if (file != null && file.getOriginalFilename().contains("..")) {
                genericResponse.setResponseCode("666");
                genericResponse.setResponseMessage("Filename contains invalid path sequence " + file.getOriginalFilename());
                genericResponse.setOutputPayload("");
            }

            String fileElement = storeFile(file);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(fileBasePath)
                    .append("/downloadFile/")
                    .append(fileElement)
                    .toString();
            String fileUrl = stringBuilder.toString();

            // save file path to database
            model.setFileName(fileElement)
                    .setFileSize(file.getSize())
                    .setFileUrl(fileUrl)
                    .setFileType(type)
                    .setCreatedDate("")
                    .setApplicationId(applicationId);
            int isFileDetailsSaved = fileStorageRepository.save(model);
            log.info("Is file details saved {} ", isFileDetailsSaved);

            // Response
            fileStorageResponse.setFileName(fileElement)
                    .setSize(file.getSize())
                    .setFileDownloadUri(fileUrl)
                    .setFileType(type);

            if (isFileDetailsSaved >= 1) {
                genericResponse.setResponseCode("000");
                genericResponse.setResponseMessage("File successfully saved");
                genericResponse.setOutputPayload(fileStorageResponse);

            } else {
                genericResponse.setResponseCode("666");
                genericResponse.setResponseMessage("Failed to save file");
                genericResponse.setOutputPayload("");
            }
        } catch (Exception exception) {
            log.info("Error occurred while saving file", exception.fillInStackTrace());
            genericResponse.setResponseCode("999");
            genericResponse.setResponseMessage("Error occurred while saving file");
            genericResponse.setOutputPayload("");
        }
        return genericResponse;
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}
