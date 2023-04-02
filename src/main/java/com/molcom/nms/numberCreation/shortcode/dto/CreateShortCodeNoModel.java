package com.molcom.nms.numberCreation.shortcode.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateShortCodeNoModel {
    @ApiModelProperty(example = "Short-Code")
    private String numberType;
    @ApiModelProperty(example = "3 Digit Service")
    private String shortCodeCategory;
    @ApiModelProperty(example = "Emergency and Security Code")
    private String shortCodeService;
    @ApiModelProperty(example = "200")
    private String minimumNumber;
    @ApiModelProperty(example = "500")
    private String maximumNumber;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @ApiModelProperty(example = "SYSTEM")
    private String createdBy;
    @Nullable
    private String createdShortCodeNumberId;
    @Nullable
    private String bulkUploadId;

}


