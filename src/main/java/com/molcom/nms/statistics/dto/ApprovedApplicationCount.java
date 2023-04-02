package com.molcom.nms.statistics.dto;

import lombok.Data;

@Data
public class ApprovedApplicationCount {
    private int standardNumbers;
    private int ISPCs;
    private int shortCodes;
    private int specialNumbers;
}
