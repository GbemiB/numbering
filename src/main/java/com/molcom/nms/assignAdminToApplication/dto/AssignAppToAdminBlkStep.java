package com.molcom.nms.assignAdminToApplication.dto;

import lombok.Data;

import java.util.List;

@Data
public class AssignAppToAdminBlkStep {
    private List<AssignAdminToAppModel> assignApplication;
}
