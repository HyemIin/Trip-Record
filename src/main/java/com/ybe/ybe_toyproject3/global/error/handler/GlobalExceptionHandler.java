package com.ybe.ybe_toyproject3.global.error.handler;

import com.ybe.ybe_toyproject3.global.error.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.CONFLICT;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected final ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .errorCode(CONFLICT.value())
                        .message(builder.toString()).build(),
                CONFLICT);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected final ResponseEntity<ErrorResponse> handleInvalidHttpMessage(
            HttpMessageNotReadableException ex, WebRequest request
    ) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .errorCode(CONFLICT.value())
                        .message(ex.getMessage())
                        .build()
                , CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected final ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request
    ) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .errorCode(CONFLICT.value())
                        .message(ex.getMessage())
                        .build()
                , CONFLICT);
    }

    @ExceptionHandler(RuntimeException.class)
    protected final ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, WebRequest request
    ) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .errorCode(CONFLICT.value())
                        .message(ex.getMessage())
                        .build()
                , CONFLICT);
    }
}