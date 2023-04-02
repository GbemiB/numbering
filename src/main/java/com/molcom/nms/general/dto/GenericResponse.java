package com.molcom.nms.general.dto;

import lombok.Data;

@Data
public class GenericResponse<T> {
    private String responseCode;
    private String responseMessage;
    private Object outputPayload;

}
