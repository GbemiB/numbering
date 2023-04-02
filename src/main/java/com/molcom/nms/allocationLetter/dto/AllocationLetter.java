package com.molcom.nms.allocationLetter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllocationLetter {
    @ApiModelProperty(example = "A000000999")
    private String applicationId;
    @ApiModelProperty(example = "allocation_letter_link")
    private String allocationLetterLink;
    @Nullable
    private String id;
}
