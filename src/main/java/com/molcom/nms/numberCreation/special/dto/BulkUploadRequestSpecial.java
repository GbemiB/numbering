package com.molcom.nms.numberCreation.special.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkUploadRequestSpecial {
    private String batchId;
    private String totalCount;
    private List<CreateSpecialNoModel> bulkList;
}
