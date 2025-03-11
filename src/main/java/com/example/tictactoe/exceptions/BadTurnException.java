package com.example.tictactoe.exceptions;

import org.springframework.http.HttpStatus;

public class BadTurnException extends HttpException {

    public BadTurnException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
