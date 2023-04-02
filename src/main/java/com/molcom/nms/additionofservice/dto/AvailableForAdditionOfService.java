package com.molcom.nms.additionofservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class AvailableForAdditionOfService {
    private String applicationId;
    private String shortCode;
    private String purpose;
    private String shortCodeCategory;
}
