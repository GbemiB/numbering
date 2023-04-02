package com.molcom.nms.general.exceptions;

import org.springframework.http.HttpStatus;

public class IntegrationException extends RuntimeException {
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public IntegrationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public IntegrationException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
