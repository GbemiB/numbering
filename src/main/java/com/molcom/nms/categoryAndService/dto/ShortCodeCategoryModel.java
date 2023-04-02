package com.molcom.nms.categoryAndService.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShortCodeCategoryModel {
    @ApiModelProperty(example = "3 Digit Code")
    private String categoryName;
    @ApiModelProperty(example = "001")
    private String categoryCode;
    @ApiModelProperty(example = "Fixed")
    private String categoryFrequency;
    @ApiModelProperty(example = "3")
    private String categoryNumberLength;
    @ApiModelProperty(example = "SYSTEM")
    private String createdBy;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @Nullable
    private String shortCodeCategoryId;
}
