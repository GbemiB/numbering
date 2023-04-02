package com.molcom.nms.number.configurations.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class FrequencyCoverageArea {
    @ApiModelProperty(example = "A0000000100")
    private String applicationId;
    @ApiModelProperty(example = "Daily")
    private String frequency;
    @ApiModelProperty(example = "Ijebu-Ode")
    private String coverageArea;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @Nullable
    private String frequencyCoverageId;
}
