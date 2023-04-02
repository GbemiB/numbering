package com.molcom.nms.numberReport.shortcode.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkUploadReportShortCode {
    private String batchId;
    private String totalCount;
    private List<ReportShortCodeRequest> bulkList;
}
