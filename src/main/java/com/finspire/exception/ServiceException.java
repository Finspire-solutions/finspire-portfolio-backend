package com.finspire.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ServiceException extends RuntimeException {
    @Getter
    private final HttpStatus httpStatus;
    @Getter
    private final String headerMessage;
    @Getter
    private final List<String> errors;

    public ServiceException(String message, String headerMessage, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.headerMessage = headerMessage;
        this.errors = List.of(message);
    }

    public ServiceException(List<String> errors, String headerMessage, HttpStatus httpStatus) {
        super(String.join("; ", errors));
        this.httpStatus = httpStatus;
        this.headerMessage = headerMessage;
        this.errors = errors;
    }
}