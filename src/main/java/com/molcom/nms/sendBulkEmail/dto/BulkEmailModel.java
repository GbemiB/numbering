package com.molcom.nms.sendBulkEmail.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BulkEmailModel {
    @ApiModelProperty(example = "molcom@gmail.com")
    private String recipientEmail;
    @ApiModelProperty(example = "Complete Application")
    private String mailSubject;
    @ApiModelProperty(example = "Application is completed")
    private String mailBody;
    @ApiModelProperty(example = "2022-03-12")
    private String isDispatched;
    @ApiModelProperty(example = "2022-03-12")
    private String createdDate;
    @ApiModelProperty(example = "molcom@gmail.com")
    private String bulkMailId;
}
