package com.molcom.nms.filestorage.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
public class DocumentModel {
    private String documentType;
    private MultipartFile file;
}
