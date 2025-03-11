package com.example.tictactoe.exceptions;

import org.springframework.http.HttpStatus;

public abstract class HttpException extends RuntimeException {

    public HttpException(String message) {
        super(message);
    }

    abstract public HttpStatus getStatus();
}
