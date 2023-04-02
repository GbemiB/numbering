package com.molcom.nms.numberReport.standard.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportStandardNoRequest {
    @ApiModelProperty(example = "ncc")
//    @JsonProperty("ALLOTEE")
    private String allotee;
    @ApiModelProperty(example = "2345")
//    @JsonProperty("AREA")
    private String area;
    @ApiModelProperty(example = "6930")
//    @JsonProperty("AREA CODE")
    private String areaCode;
    @ApiModelProperty(example = "0801")
//    @JsonProperty("ACCESS CODE")
    private String accessCode;
    @ApiModelProperty(example = "0000000")
//    @JsonProperty("SUB-BLOCK START")
    private String subBlockStart;
    @ApiModelProperty(example = "9999999")
//    @JsonProperty("SUB-BLOCK END")
    private String subBlockEnd;
    @ApiModelProperty(example = "ncc")
//    @JsonProperty("NUMBER SUBTYPE")
    private String numberSubType;

    @Nullable
    private String bulkUploadId;
}
