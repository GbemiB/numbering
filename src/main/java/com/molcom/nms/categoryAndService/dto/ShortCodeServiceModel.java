package com.molcom.nms.categoryAndService.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShortCodeServiceModel {
    @ApiModelProperty(example = "3 Digit Code")
    private String shortCodeCategory;
    @ApiModelProperty(example = "Emergency Services")
    private String serviceName;
    @ApiModelProperty(example = "930")
    private String serviceCode;
    @ApiModelProperty(example = "For emergency services")
    private String serviceDescription;
    @ApiModelProperty(example = "SYSTEM")
    private String createdBy;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @Nullable
    private String ShortCodeServiceId;
}
