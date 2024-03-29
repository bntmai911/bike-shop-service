package com.example.bike.shop.application.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{
    private HttpStatus httpStatus;

    public ApiException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ApiException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}
