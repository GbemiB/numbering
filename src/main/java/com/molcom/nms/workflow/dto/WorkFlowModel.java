package com.molcom.nms.workflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
public class WorkFlowModel {
    @ApiModelProperty(example = "Line Fee")
    private String nameOfWorkFlow;
    @ApiModelProperty(example = "Standard")
    private String process;
    @ApiModelProperty
    @JsonProperty("steps")
    private List<WorkFlowStep> steps;
    @Nullable
    private String workflowId;
}
