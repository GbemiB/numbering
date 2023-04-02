package com.molcom.nms.organisationprofile.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class OrganisationProfileModel {
    @ApiModelProperty(example = "CENTURY TECH LIMITED")
    private String organisationName;
    @ApiModelProperty(example = "000-0095")
    private String nccId;
    @Nullable
    private String organisationProfileId;

}
