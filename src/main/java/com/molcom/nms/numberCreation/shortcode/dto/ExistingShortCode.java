package com.molcom.nms.numberCreation.shortcode.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExistingShortCode {
    @ApiModelProperty(example = "2019")
    private String shortCode;
    @ApiModelProperty(example = "Test")
    private String operator;
    @ApiModelProperty(example = "Molcom")
    private String client;
    @ApiModelProperty(example = "4 digit code")
    private String service;
    @ApiModelProperty(example = "Emergency Services")
    private String serviceType;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String dateAllocated;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String dateOfExpiration;
    @Nullable
    private String existingShortCodeId;
}
