package com.molcom.nms.areacode.dto;

import lombok.Data;

import java.util.List;

@Data
public class AreaCodeBlkRes {
    private String batchId;
    private String totalCount;
    private List<AreaCodeBlkItem> allList;
}
