package com.molcom.nms.assignAdminToApplication.dto;

import lombok.Data;

import java.util.List;

@Data
public class AssignAppToAdminBlkRes {
    private String batchId;
    private String totalCount;
    private List<AssignAppToAdminItem> allList;
}
