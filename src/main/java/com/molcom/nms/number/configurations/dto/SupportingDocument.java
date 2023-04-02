package com.molcom.nms.number.configurations.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class SupportingDocument {
    @ApiModelProperty(example = "A0000000100")
    private String applicationId;
    @ApiModelProperty(example = "Licence")
    private String documentName;
    @ApiModelProperty(example = "Test")
    private String documentBase64String;
    @ApiModelProperty(example = "2022-10-02 08:39:19")
    private String createdDate;
    @ApiModelProperty(example = "molcon.png")
    private String documentFileName;
    @ApiModelProperty(example = "Y") // Y or N
    private String isRequired;
    @Nullable
    private String documentId;


}
