package com.molcom.nms.approvals.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApprovalSupportingDocument {
    @ApiModelProperty(example = "Approval form")
    private String documentName;
    @ApiModelProperty(example = "data:image/png;base64,KRculum3KuCadIBlP6lCbsyjG5I5F+2EkMkeSGYeT0OipnPTGm6HLdLcut")
    private String documentBase64String;

}
