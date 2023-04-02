package com.molcom.nms.accesscode.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessCodeModel {
    @ApiModelProperty(example = "1002")
    private String accessCodeName;
    @ApiModelProperty(example = "Ijebu-ode")
    private String areaCode;
    @ApiModelProperty(example = "standard")
    private String numberType;
    @ApiModelProperty(example = "Geographical")
    private String numberSubType;
    @ApiModelProperty(example = "37 -- Ijebu-Ode")
    private String coverageArea;
    @ApiModelProperty(example = "System")
    private String createdUser;
    @ApiModelProperty(example = "Active")
    private String status;
    @ApiModelProperty(example = "2022-10-02 08:39:1")
    private String createdDate;
    @ApiModelProperty(example = "System")
    private String updatedBy;
    @ApiModelProperty(example = "2022-10-02 08:39:1")
    private String updatedDate;
    @Nullable
    private String accessCodeId;
    @Nullable
    private String bulkUploadId;

}
