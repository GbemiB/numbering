package com.molcom.nms.numberCreation.standard.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateStandardNoModel {
    @ApiModelProperty(example = "Standard")
    private String numberType;
    @ApiModelProperty(example = "National")
    private String numberSubType;
    @ApiModelProperty(example = "10")
    private String coverageArea;
    @ApiModelProperty(example = "900")
    private String areaCode;
    @ApiModelProperty(example = "010")
    private String accessCode;
    @ApiModelProperty(example = "00000000")
    private String minimumNumber;
    @ApiModelProperty(example = "99999999")
    private String maximumNumber;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @ApiModelProperty(example = "SYSTEM")
    private String createdBy;
    @Nullable
    private String createdStandardNumberId;
    @Nullable
    private String bulkUploadId;

}


