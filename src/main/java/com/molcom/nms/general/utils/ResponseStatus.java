package com.molcom.nms.general.utils;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    ALREADY_EXIST("666", "ALREADY EXIST"),
    SUCCESS("000", "SUCCESS"),
    NOT_FOUND("777", "NOT FOUND"),
    FAILED("888", "FAILED"),
    ERROR_OCCURRED("999", "ERROR OCCURRED"),
    EXTERNAL_INTEGRATION_ERROR("555", "EXTERNAL INTEGRATION ERROR"),
    PAYMENT_MOCK_FAILED("444", "MOCK PAYMENT FAILED"),
    EXPIRATION_MOCK_FAILED("333", "APPLICATION EXPIRATION FAILED"),
    UNAUTHORIZED("401", "UNAUTHORIZED"),
    AUTHORIZED("201", "AUTHORIZED");


    private final String code;
    private String message;

    ResponseStatus(String code) {
        this.code = code;
    }

    ResponseStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
