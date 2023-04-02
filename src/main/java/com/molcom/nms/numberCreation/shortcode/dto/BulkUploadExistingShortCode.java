package com.molcom.nms.numberCreation.shortcode.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkUploadExistingShortCode {
    private String batchId;
    private String totalCount;
    private List<ExistingShortCode> bulkList;
}
