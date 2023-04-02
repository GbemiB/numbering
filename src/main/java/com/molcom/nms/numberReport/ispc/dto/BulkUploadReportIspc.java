package com.molcom.nms.numberReport.ispc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkUploadReportIspc {
    private String batchId;
    private String totalCount;
    private List<ReportIspcNoRequest> bulkList;
}
