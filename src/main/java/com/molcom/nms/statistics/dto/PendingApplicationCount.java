package com.molcom.nms.statistics.dto;

import lombok.Data;

@Data
public class PendingApplicationCount {
    private int standardNumbers;
    private int ISPCs;
    private int shortCodes;
    private int specialNumbers;
}
