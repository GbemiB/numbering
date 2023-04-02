package com.molcom.nms.areacode.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AreaCodeModel {
    @ApiModelProperty(example = "30") // not duplicate
    private String areaCode;
    @ApiModelProperty(example = "Eti-osa")
    private String coverageArea;
    @ApiModelProperty(example = "System")
    private String createdUser;
    @ApiModelProperty(example = "2022-10-02 08:39:1")
    private String createdDate;
    @ApiModelProperty(example = "System")
    private String updateBy;
    @ApiModelProperty(example = "2022-10-02 08:39:1")
    private String updatedDate;
    @Nullable
    private String areaId;
    @Nullable
    private String bulkUploadId;

}
