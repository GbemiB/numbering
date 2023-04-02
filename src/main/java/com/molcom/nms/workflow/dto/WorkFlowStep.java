package com.molcom.nms.workflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkFlowStep {
    @JsonProperty("stepNum")
    @ApiModelProperty(name = "stepNum")
    private String workFlowStepNum;
    @JsonProperty("stepName")
    private String workFlowStepName;
    @JsonProperty("roles")
    private List<String> roles;
    @JsonProperty("generateInvoice")
    private String generateInvoice;
}
