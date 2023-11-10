package com.ybe.ybe_toyproject3.global.security.token.exception;

public class ExpireAccessTokenException extends IllegalArgumentException {
    public ExpireAccessTokenException() {
        super("만료된 엑세스 토큰입니다.");
    }

    public ExpireAccessTokenException(String s) {
        super(s);
    }
}
