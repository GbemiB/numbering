package com.molcom.nms.rolesandpriviledge.userroles.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRoleModel {
    @ApiModelProperty(example = "COLLABORATOR ADMIN")
    private String roleType;
    @ApiModelProperty(example = "Numbering Admin")
    private String createdBy;
    @ApiModelProperty(example = "Numbering Admin")
    private String updatedBy;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String modifiedDate;
    @Nullable
    private String userRoleId;
    @Nullable
    private String UserTypeId;
}


