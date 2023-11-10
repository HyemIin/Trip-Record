package com.ybe.ybe_toyproject3.domain.trip.exception;

import lombok.Getter;

@Getter
public class DuplicateTripNameException extends RuntimeException {
    public DuplicateTripNameException(String message) {
        super(message);
    }
}
