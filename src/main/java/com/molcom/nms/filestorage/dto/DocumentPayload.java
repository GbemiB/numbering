package com.molcom.nms.filestorage.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
public class DocumentPayload {
    private String applicationId;
    private String createdUser;
    private List<DocumentModel> document = new ArrayList<>();
}
