package com.example.tictactoe.exceptions;

import org.springframework.http.HttpStatus;

public class WinnerException extends HttpException {
    public WinnerException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CREATED;
    }
}
