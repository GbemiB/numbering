package com.molcom.nms.adminmanage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminManagementModel {
    private String userId;

    @ApiModelProperty(example = "Steve Test")
    private String name;

    @ApiModelProperty(example = "steve31u@gmail.com")
    private String email;

    @ApiModelProperty(example = "stevetest")
    private String userName;

    @ApiModelProperty(example = "Senior Manager")
    private String assignedRole;

    @ApiModelProperty(example = "TEST")
    private String signature;

    @ApiModelProperty(example = "Molcom")
    private String organisation;

    @Nullable
    private String adminManageId;

}
