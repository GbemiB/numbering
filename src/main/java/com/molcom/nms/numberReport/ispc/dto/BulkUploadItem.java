package com.molcom.nms.numberReport.ispc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkUploadItem {
    private String itemId;
    private String itemResCode;
    private String itemResMessage;
}
