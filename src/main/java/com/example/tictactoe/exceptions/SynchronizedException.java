package com.example.tictactoe.exceptions;

import org.springframework.http.HttpStatus;

public class SynchronizedException extends HttpException {

    public SynchronizedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
