package com.example.tictactoe.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(HttpException.class)
    @ResponseBody
    public String handleHttpException(HttpException ex, HttpServletResponse response) {
        response.setStatus(ex.getStatus().value());
        return ex.getMessage();
    }
}
