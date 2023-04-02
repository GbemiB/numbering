package com.molcom.nms.numberReport.ispc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportIspcNoRequest {
    @ApiModelProperty(example = "ncc")
//    @JsonProperty("ALLOTEE")
    private String allotee;
    @ApiModelProperty(example = "2345")
//    @JsonProperty("ISPC NUMBER")
    private String ispcNumber;
    @Nullable
    private String bulkUploadId;
}
