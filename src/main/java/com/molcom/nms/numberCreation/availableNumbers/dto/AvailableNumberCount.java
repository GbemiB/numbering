package com.molcom.nms.numberCreation.availableNumbers.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvailableNumberCount {
    List<AvailableNumberBlockModel> numberBlock;
    @ApiModelProperty(example = "10")
    private int countAvailable;
}
