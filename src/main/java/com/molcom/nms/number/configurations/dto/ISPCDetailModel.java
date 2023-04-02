package com.molcom.nms.number.configurations.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class ISPCDetailModel {
    @ApiModelProperty(example = "A0000000100")
    private String applicationId;
    @ApiModelProperty(example = "Test")
    private String switchName;
    @ApiModelProperty(example = "Test Tyoe")
    private String switchType;
    @ApiModelProperty(example = "Test Function")
    private String switchFunction;
    @ApiModelProperty(example = "Test Location")
    private String switchLocation;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String commissionDate;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @Nullable
    private String ispcDetailId;
}
