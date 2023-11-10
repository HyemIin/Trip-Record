package com.ybe.ybe_toyproject3.domain.trip.exception;

public class InvalidTripScheduleException extends IllegalArgumentException {
    public InvalidTripScheduleException() {
        super("입력한 여행 일정 기간이 유효하지 않은 범위입니다.");
    }

    public InvalidTripScheduleException(String s) {
        super(s);
    }
}
