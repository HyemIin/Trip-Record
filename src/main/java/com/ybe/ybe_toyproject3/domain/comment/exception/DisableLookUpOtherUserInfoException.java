package com.ybe.ybe_toyproject3.domain.comment.exception;

public class DisableLookUpOtherUserInfoException extends IllegalArgumentException{
    public DisableLookUpOtherUserInfoException() {
        super("다른 사용자의 댓글 정보에 접근할 수 없습니다.");
    }

    public DisableLookUpOtherUserInfoException(String s) {
        super(s);
    }
}
