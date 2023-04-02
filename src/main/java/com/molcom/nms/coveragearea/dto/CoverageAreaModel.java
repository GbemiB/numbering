package com.molcom.nms.coveragearea.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoverageAreaModel {
    @ApiModelProperty(example = "Eti-osa")
    private String coverageName;
    @ApiModelProperty(example = "National")
    private String coverageType;
    @ApiModelProperty(example = "Coverage area for eti-osa local government lagos state")
    private String coverageDescription;
    @ApiModelProperty(example = "System")
    private String createdUser;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @ApiModelProperty(example = "System")
    private String updatedBy;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String updatedDate;
    @Nullable
    private String coverageId;
    @Nullable
    private String bulkUploadId;
}
