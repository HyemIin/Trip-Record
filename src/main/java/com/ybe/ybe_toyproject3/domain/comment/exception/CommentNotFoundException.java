package com.ybe.ybe_toyproject3.domain.comment.exception;

public class CommentNotFoundException extends IllegalArgumentException {
    public CommentNotFoundException() {
        super("댓글이 이미 삭제되었거나 존재하지 않는 댓글입니다.");
    }

    public CommentNotFoundException(String s) {
        super(s);
    }
}
