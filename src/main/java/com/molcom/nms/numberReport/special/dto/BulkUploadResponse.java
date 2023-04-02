package com.molcom.nms.numberReport.special.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkUploadResponse {
    private String batchId;
    private String totalCount;
    private List<BulkUploadItem> allList;
}
