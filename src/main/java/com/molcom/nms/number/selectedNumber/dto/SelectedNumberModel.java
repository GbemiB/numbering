package com.molcom.nms.number.selectedNumber.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectedNumberModel {
    @ApiModelProperty(example = "A0000000100")
    private String applicationId;
    @ApiModelProperty(example = "0103830-484940")
    private String selectedNumberValue;
    @ApiModelProperty(example = "Standard")
    private String numberType;
    @ApiModelProperty(example = "National")
    private String numberSubType;
    @ApiModelProperty(example = "Business")
    private String purpose;
    @ApiModelProperty(example = "Test")
    private String tariff;
    @ApiModelProperty(example = "Test")
    private String bearerMedium;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @Nullable
    private String selectedNumberId;
    @Nullable
    private String companyAllocatedTo;
    @Nullable
    private String dateAllocated;
    @Nullable
    private String allocationValidityFrom;
    @Nullable
    private String allocationValidityTo;
    @Nullable
    private String allocationStatus;


}
