package com.molcom.nms.workflow.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowObject {
    @ApiModelProperty(example = "Short code steps")
    private String nameOfWorkFlow;
    private List<String> process = new ArrayList<>();
    @JsonProperty("steps")
    private List<WorkFlowStep> steps = new ArrayList<>();
    @Nullable
    private String workflowId;
}
