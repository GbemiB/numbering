package com.molcom.nms.accesscode.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccessCodeBlkReq {
    private String batchId;
    private String totalCount;
    private List<AccessCodeModel> bulkList;
}
