package com.molcom.nms.number.configurations.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class DeviceModel {
    @ApiModelProperty(example = "A0000000100")
    private String applicationId;
    @ApiModelProperty(example = "Cable Wire")
    private String deviceName;
    @ApiModelProperty(example = "2022-10-02")
    private String connectionType;
    @ApiModelProperty(example = "Cable")
    private String deviceType;
    @ApiModelProperty(example = "ZNE-Samsung")
    private String deviceModel;
    @ApiModelProperty(example = "Samsung")
    private String manufacturer;
    @ApiModelProperty(example = "System")
    private String createdBy;
    @ApiModelProperty(example = "2022-10-02")
    private String createdDate;
    @Nullable
    private String deviceId;
}
