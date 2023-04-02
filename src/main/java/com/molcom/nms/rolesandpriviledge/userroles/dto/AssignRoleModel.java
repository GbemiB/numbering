package com.molcom.nms.rolesandpriviledge.userroles.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class AssignRoleModel {
    @ApiModelProperty(example = "Molcom_admin")
    private String Username;
    @ApiModelProperty(example = "COLLABORATOR ADMIN")
    private String roleName;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @Nullable
    private String UserAssignedRoles;
}
