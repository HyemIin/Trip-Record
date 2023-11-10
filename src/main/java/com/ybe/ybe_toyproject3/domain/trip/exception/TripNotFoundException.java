package com.ybe.ybe_toyproject3.domain.trip.exception;

import lombok.Getter;

@Getter
public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException() {
        super("입력하신 아이디에 대한 여행이 존재하지 않습니다.");
    }

    public TripNotFoundException(String s) {
        super(s);
    }
}