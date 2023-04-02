package com.molcom.nms.numberCreation.special.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class CreateSpecialNoModel {
    @ApiModelProperty(example = "Special")
    private String numberType;
    @ApiModelProperty(example = "Vanity")
    private String numberSubType;
    @ApiModelProperty(example = "500")
    private String accessCode;
    @ApiModelProperty(example = "00000000")
    private String minimumNumber;
    @ApiModelProperty(example = "99999999")
    private String maximumNumber;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @ApiModelProperty(example = "SYSTEM")
    private String createdBy;
    @Nullable
    private String createdSpecialNumberId;
    @Nullable
    private String bulkUploadId;

}


