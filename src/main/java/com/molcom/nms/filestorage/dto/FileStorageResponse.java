package com.molcom.nms.filestorage.dto;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FileStorageResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public FileStorageResponse(String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

    public FileStorageResponse() {
    }
}
