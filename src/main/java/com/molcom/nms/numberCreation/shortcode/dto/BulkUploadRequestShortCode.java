package com.molcom.nms.numberCreation.shortcode.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkUploadRequestShortCode {
    private String batchId;
    private String totalCount;
    private List<CreateShortCodeNoModel> bulkList;
}
