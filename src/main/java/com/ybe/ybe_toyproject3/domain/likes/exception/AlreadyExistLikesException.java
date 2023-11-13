package com.ybe.ybe_toyproject3.domain.likes.exception;

public class AlreadyExistLikesException extends RuntimeException {
    public AlreadyExistLikesException(String message){
        super(message);
    }
}
