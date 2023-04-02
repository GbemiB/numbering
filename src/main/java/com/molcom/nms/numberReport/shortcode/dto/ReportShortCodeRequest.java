package com.molcom.nms.numberReport.shortcode.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportShortCodeRequest {

    @ApiModelProperty(example = "ncc")
//    @JsonProperty("ALLOTEE")
    private String allotee;
    @ApiModelProperty(example = "Emergency")
//    @JsonProperty("SERVICE")
    private String service;
    @ApiModelProperty(example = "100")
//    @JsonProperty("SHORTCODE NUMBER")
    private String shortCodeNumber;

    @Nullable
    private String bulkUploadId;

}
