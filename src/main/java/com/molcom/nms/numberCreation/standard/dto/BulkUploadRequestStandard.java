package com.molcom.nms.numberCreation.standard.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkUploadRequestStandard {
    private String batchId;
    private String totalCount;
    private List<CreateStandardNoModel> bulkList;
}
