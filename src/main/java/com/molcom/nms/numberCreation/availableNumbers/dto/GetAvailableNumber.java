package com.molcom.nms.numberCreation.availableNumbers.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAvailableNumber {
    @ApiModelProperty(example = "Special")
    private String numberType;
    @ApiModelProperty(example = "Vanity")
    private String numberSubType;
    @Nullable
    @ApiModelProperty(example = "010")
    private String accessCode;
    @Nullable
    @ApiModelProperty(example = "Emergency")
    private String shortCodeServiceName;
}
