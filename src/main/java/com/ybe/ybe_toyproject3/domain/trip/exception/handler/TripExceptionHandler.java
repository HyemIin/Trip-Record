package com.ybe.ybe_toyproject3.domain.trip.exception.handler;

import com.ybe.ybe_toyproject3.domain.trip.exception.DuplicateTripNameException;
import com.ybe.ybe_toyproject3.domain.trip.exception.InvalidTripScheduleException;
import com.ybe.ybe_toyproject3.domain.trip.exception.NullTripListException;
import com.ybe.ybe_toyproject3.domain.trip.exception.TripNotFoundException;
import com.ybe.ybe_toyproject3.global.error.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.format.DateTimeParseException;

import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@RestControllerAdvice
public class TripExceptionHandler {
    @ExceptionHandler(InvalidTripScheduleException.class)
    protected final ResponseEntity<ErrorResponse> handleInvalidTripScheduleException(
            InvalidTripScheduleException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(CONFLICT.value())
                .message(ex.getMessage())
                .build()
                , CONFLICT);
    }

    @ExceptionHandler(DateTimeParseException.class)
    protected final ResponseEntity<ErrorResponse> handleInvalidDateFormatException(
            DateTimeParseException ex, WebRequest request
    ) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(CONFLICT.value())
                .message(ex.getMessage())
                .build()
                , CONFLICT);
    }

    @ExceptionHandler(DuplicateTripNameException.class)
    protected final ResponseEntity<ErrorResponse> handleDuplicateTripNameException(
            DuplicateTripNameException ex
    ) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(CONFLICT.value())
                .message(ex.getMessage())
                .build()
                , CONFLICT);
    }

    @ExceptionHandler(NullTripListException.class)
    protected final ResponseEntity<ErrorResponse> handleNullTripListException(
            NullTripListException ne
    ) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(CONFLICT.value())
                .message(ne.getMessage())
                .build()
                , CONFLICT);
    }

    @ExceptionHandler(TripNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handleTripNotFoundException(
            TripNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(CONFLICT.value())
                .message(ex.getMessage())
                .build()
                , CONFLICT);
    }
}