package com.molcom.nms.accesscode.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccessCodeBlkRes {
    private String batchId;
    private String totalCount;
    private List<AccessCodeBlkItem> allList;
}
