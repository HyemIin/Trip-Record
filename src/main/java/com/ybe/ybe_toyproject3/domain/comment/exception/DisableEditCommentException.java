package com.ybe.ybe_toyproject3.domain.comment.exception;

public class DisableEditCommentException extends IllegalArgumentException {
    public DisableEditCommentException() {
        super("직접 작성한 댓글만 수정할 수 있습니다.");
    }

    public DisableEditCommentException(String s) {
        super(s);
    }
}
