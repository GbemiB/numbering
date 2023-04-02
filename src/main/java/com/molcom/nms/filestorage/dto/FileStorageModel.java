package com.molcom.nms.filestorage.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
public class FileStorageModel {
    private String fileId;
    private String fileName;
    private Long fileSize;
    private String fileUrl;
    private String applicationId;
    private String fileType;
    private String createdDate;
}
