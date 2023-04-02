package com.molcom.nms.fee.type.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;


@Data
public class FeeTypeModel {
    @ApiModelProperty(example = "Value Added Services")
    private String feeTypeName;
    @ApiModelProperty(example = "System")
    private String createdUser;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @Nullable
    private String feeTypeId;
}

