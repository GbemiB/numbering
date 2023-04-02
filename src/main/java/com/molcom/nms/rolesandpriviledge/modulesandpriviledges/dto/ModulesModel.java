package com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModulesModel {
    @ApiModelProperty(example = "SENIOR COLLABORATOR")
    private String roleType;
    @ApiModelProperty(example = "Admins")
    private String modules;
    @ApiModelProperty(example = "true")
    private String isCreated; // true or false
    @ApiModelProperty(example = "true")
    private String isReadAllow; // Y or N
    @ApiModelProperty(example = "true")
    private String isUpdatedAllow; // Y or N
    @ApiModelProperty(example = "true")
    private String isDeleteAllow; // Y or N
    @ApiModelProperty(example = "Numbering Admin")
    private String createdBy;
    @ApiModelProperty(example = "Numbering Admin")
    private String updatedBy;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String updatedDate;
    @Nullable
    private String modulesId;
    @Nullable
    private String bulkUploadId;
}
