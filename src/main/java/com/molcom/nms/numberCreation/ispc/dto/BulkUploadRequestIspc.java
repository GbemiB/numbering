package com.molcom.nms.numberCreation.ispc.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkUploadRequestIspc {
    private String batchId;
    private String totalCount;
    private List<CreateIspcNoModel> bulkList;
}
