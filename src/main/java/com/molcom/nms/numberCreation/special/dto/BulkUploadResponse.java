package com.molcom.nms.numberCreation.special.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkUploadResponse {
    private String batchId;
    private String totalCount;
    private List<BulkUploadItem> allList;
}
