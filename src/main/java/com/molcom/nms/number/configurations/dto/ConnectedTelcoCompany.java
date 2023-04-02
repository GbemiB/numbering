package com.molcom.nms.number.configurations.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class ConnectedTelcoCompany {
    @ApiModelProperty(example = "A0000000100")
    private String applicationId;
    @ApiModelProperty(example = "Star Way")
    private String nameOfCompany;
    @ApiModelProperty(example = "Mtn")
    private String pointOfConnection;
    @ApiModelProperty(example = "2022-10-02")
    private String dateSelector;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @Nullable
    private String interconnectId;
}
