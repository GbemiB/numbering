package com.molcom.nms.number.configurations.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class EquipmentDetailModel {
    @ApiModelProperty(example = "A0000000100")
    private String applicationId;
    @ApiModelProperty(example = "Cables")
    private String equipmentName;
    @ApiModelProperty(example = "2022-10-02")
    private String dateSelector;
    @ApiModelProperty(example = "Connector")
    private String equipmentType;
    @ApiModelProperty(example = "ZN460-BRAND-WAY")
    private String equipmentModel;
    @ApiModelProperty(example = "Goggle")
    private String equipmentManufacturer;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @Nullable
    private String equipmentId;
}
