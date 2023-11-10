package com.ybe.ybe_toyproject3.domain.user.exception.handler;

import com.ybe.ybe_toyproject3.domain.user.exception.UserNotFoundException;
import com.ybe.ybe_toyproject3.global.error.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.CONFLICT;

@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex, WebRequest request
    ) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .errorCode(CONFLICT.value())
                        .message(ex.getMessage())
                        .build()
                , CONFLICT);
    }
}
