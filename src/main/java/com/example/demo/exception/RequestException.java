package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class RequestException extends RuntimeException {

    //è semplicemente una classe che rappresenta un tipo di errore
    private final HttpStatus httpStatus;

    public RequestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
