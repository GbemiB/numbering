package com.molcom.nms.numberCreation.ispc.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class CreateIspcNoModel {
    @ApiModelProperty(example = "1")
    private String ispcNumber;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @ApiModelProperty(example = "SYSTEM")
    private String createdBy;
    @Nullable
    private String createdIspcNumberId;
    @Nullable
    private String bulkUploadId;

}
