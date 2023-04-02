package com.molcom.nms.rolesandpriviledge.userroles.dto;

import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto.BulkUploadRequestModule;
import lombok.Data;

@Data
public class UserObjectModel {
    private UserRoleModel userRoles = new UserRoleModel();
    private BulkUploadRequestModule assignedModule = new BulkUploadRequestModule();
}
