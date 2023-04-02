package com.molcom.nms.statistics.dto;

import lombok.Data;

@Data
public class RecentApplicationLog {
    private String applicationId;
    private String numberType;
    private String applicationStatus;
    private String createdBy;
    private String createdUserEmail;
    private String createdDate;
}
