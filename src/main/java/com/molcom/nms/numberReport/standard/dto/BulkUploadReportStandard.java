package com.molcom.nms.numberReport.standard.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkUploadReportStandard {
    private String batchId;
    private String totalCount;
    private List<ReportStandardNoRequest> bulkList;
}
