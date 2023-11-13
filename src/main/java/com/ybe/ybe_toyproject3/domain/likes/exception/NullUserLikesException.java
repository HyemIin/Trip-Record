package com.ybe.ybe_toyproject3.domain.likes.exception;

public class NullUserLikesException extends RuntimeException {
    public NullUserLikesException(String message) {
        super(message);
    }
}