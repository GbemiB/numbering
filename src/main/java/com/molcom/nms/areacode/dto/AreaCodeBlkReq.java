package com.molcom.nms.areacode.dto;

import lombok.Data;

import java.util.List;

@Data
public class AreaCodeBlkReq {
    private String batchId;
    private String totalCount;
    private List<AreaCodeModel> bulkList;
}
