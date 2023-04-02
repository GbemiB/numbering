package com.molcom.nms.numberCreation.availableNumbers.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAvailableNumberCount {
    @ApiModelProperty(example = "Standard", notes = "mandatory")
    private String numberType;
    @ApiModelProperty(example = "National", notes = "mandatory")
    private String subType;
    @ApiModelProperty(example = "100", notes = "For special, standard and short code numbers")
    private String minimumNumber;
    @ApiModelProperty(example = "500", notes = "For special, standard and short code numbers")
    private String maximumNumber;
    @ApiModelProperty(example = "104", notes = "Only for short code")
    private String shortCodeNumber;
    @ApiModelProperty(example = "Emergency and Security Code", notes = "Only for short code")
    private String shortCodeService;
    @ApiModelProperty(example = "5000")
    private String accessCode;
}
