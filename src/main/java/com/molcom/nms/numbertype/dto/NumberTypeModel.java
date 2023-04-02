package com.molcom.nms.numbertype.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NumberTypeModel {
    @ApiModelProperty(example = "Standard")
    private String numberType;
    @ApiModelProperty(example = "Geographical")
    private String numberSubType;
    @ApiModelProperty(example = "Every Year")
    private String billingFrequency;
    @ApiModelProperty(example = "NMS Admin")
    private String createdUser;
    @ApiModelProperty(example = "NMS Admin")
    private String updatedBy;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String updatedDate;
    @Nullable
    private String numberTypeId;
}
