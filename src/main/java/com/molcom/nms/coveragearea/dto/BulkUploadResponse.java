package com.molcom.nms.coveragearea.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkUploadResponse {
    private String batchId;
    private String totalCount;
    private List<BulkUploadItem> allList;
}
