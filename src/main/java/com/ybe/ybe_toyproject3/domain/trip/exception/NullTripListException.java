package com.ybe.ybe_toyproject3.domain.trip.exception;

public class NullTripListException extends IllegalArgumentException {
    public NullTripListException() {
        super("해당 여행id는 이미 삭제되었거나 존재하지 않습니다.");
    }
}
