package com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkUploadResponseModel {
    private String batchId;
    private String totalCount;
    private List<BulkUploadItem> allList;
}
