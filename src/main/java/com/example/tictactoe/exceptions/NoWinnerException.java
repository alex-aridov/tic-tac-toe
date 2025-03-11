package com.example.tictactoe.exceptions;

import org.springframework.http.HttpStatus;

public class NoWinnerException extends HttpException {
    public NoWinnerException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CREATED;
    }
}
