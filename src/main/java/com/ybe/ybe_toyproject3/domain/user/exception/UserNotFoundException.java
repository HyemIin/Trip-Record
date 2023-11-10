package com.ybe.ybe_toyproject3.domain.user.exception;

public class UserNotFoundException extends IllegalArgumentException {
    public UserNotFoundException() {
        super("사용자를 찾을 수 없습니다.");
    }

    public UserNotFoundException(String s) {
        super(s);
    }
}
