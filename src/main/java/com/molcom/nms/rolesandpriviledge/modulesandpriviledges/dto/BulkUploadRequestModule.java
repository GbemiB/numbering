package com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkUploadRequestModule {
    private List<ModulesModel> moduleList;
}
